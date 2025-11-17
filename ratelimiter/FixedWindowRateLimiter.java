package ratelimiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 固定窗口限流
 */
public class FixedWindowRateLimiter implements RateLimiter {

    private final int limit;
    private final int windowSize;
    private final TimeUnit timeUnit;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    /**
     * 当前窗口的结束时间
     */
    private long currentWindowEnd;
    /**
     * 当前窗口的计数
     */
    private int count;

    /**
     * @param limit      窗口限流数量
     * @param windowSize 窗口大小
     * @param timeUnit   窗口大小单位
     */

    public FixedWindowRateLimiter(int limit, int windowSize, TimeUnit timeUnit) {
        this.limit = limit;
        this.windowSize = windowSize;
        this.timeUnit = timeUnit;
        this.currentWindowEnd = System.nanoTime() + timeUnit.toNanos(windowSize);
        this.count = 0;
    }

    @Override
    public Boolean acquire(long timeout, TimeUnit timeUnit) throws InterruptedException {
        long timeoutNanos = timeUnit.toNanos(timeout);
        long deadline = System.nanoTime() + timeoutNanos;
        try{
            lock.lock();
            while (!tryAcquire() && !Thread.currentThread().isInterrupted()) {
                long now = System.nanoTime();
                if (now >= deadline) {
                    return false;
                }
                long needWait = Math.min((currentWindowEnd - now),
                        (deadline - now));

                if (needWait <= 0) {
                    continue;
                }
                condition.awaitNanos(needWait);
            }
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    public Boolean acquire() throws InterruptedException {
        try {
            lock.lock();
            while (!tryAcquire() && !Thread.currentThread().isInterrupted()) {
                long now = System.nanoTime();
                //计算需要等待的时间
                long needWait = (currentWindowEnd - now);
                condition.awaitNanos(needWait);
            }
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    public Boolean tryAcquire() {
        try {
            lock.lock();
            long now = System.nanoTime();
            //切换到新的窗口
            while (now > currentWindowEnd) {
                currentWindowEnd = currentWindowEnd + timeUnit.toNanos(windowSize);
                count = 0;
                //切换窗口时候通知所有等待的线程
                condition.signalAll();
            }
            if (count < limit) {
                count++;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }

    }
}
