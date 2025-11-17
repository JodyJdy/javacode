package ratelimiter;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowRateLimiter implements RateLimiter{

    /**
     * 窗口限流数量
     */
    private final int limit;
    private final int windowSize;
    private final TimeUnit timeUnit;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    /**
     * 存储请求时间
     */
    private final LinkedList<Long> requestNanoTimes = new LinkedList<>();


    public SlidingWindowRateLimiter(int limit, int windowSize, TimeUnit timeUnit) {
        this.limit = limit;
        this.windowSize = windowSize;
        this.timeUnit = timeUnit;
    }



    @Override
    public Boolean acquire(long timeout, TimeUnit timeUnit) throws InterruptedException {
        long timeoutNanos = timeUnit.toNanos(timeout);
        long deadline =  System.nanoTime() + timeoutNanos;
        try {
            lock.lock();
            while (!tryAcquire() && !Thread.currentThread().isInterrupted()) {
                if(System.nanoTime() > deadline){
                    return false;
                }
                // 计算请求的时间,  req1->req2->req3, 如果知道re1,req2的间隔，那么等待对应的时间即可
                if (!requestNanoTimes.isEmpty()) {
                    long firstRequestTime = requestNanoTimes.peekFirst();
                    if (requestNanoTimes.size() > 2) {
                        Long secondRequestTime = requestNanoTimes.get(1);
                        condition.awaitNanos(Math.min(secondRequestTime - firstRequestTime,deadline - System.nanoTime()));
                    } else{
                        condition.awaitNanos(deadline - System.nanoTime());
                    }
                }
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
                // 计算请求的时间,  req1->req2->req3, 如果知道re1,req2的间隔，那么等待对应的时间即可
                if (!requestNanoTimes.isEmpty()) {
                    long firstRequestTime = requestNanoTimes.peekFirst();
                    if (requestNanoTimes.size() > 2) {
                        Long secondRequestTime = requestNanoTimes.get(1);
                        condition.awaitNanos(Math.max(secondRequestTime - firstRequestTime,10));
                    } else{
                        condition.awaitNanos(50);
                    }
                }
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

            //将窗口外的请求时间移除
            while (!requestNanoTimes.isEmpty() && (now - requestNanoTimes.peekFirst()) > timeUnit.toNanos(windowSize)) {
                requestNanoTimes.removeFirst();
                condition.signalAll();
            }

            if (requestNanoTimes.size() < limit) {
                requestNanoTimes.add(now);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }

    }
}
