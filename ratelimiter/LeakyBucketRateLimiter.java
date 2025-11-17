package ratelimiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *漏桶算法
 */
public class LeakyBucketRateLimiter implements RateLimiter {

    /**
     * 水桶容量
     */
    private final int limit;

    /**
     * 漏水速率
     */
    private final double rate;
    /**
     * 每 per 时间，漏水 rate
     */
    private final int perTime;
    /**
     * 漏水时间计算单位
     */
    private final TimeUnit timeUnit;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    /**
     * 漏桶中的水量
     */
    private double water;

    /**
     * 上次漏水时间
     */
    private long lastLeakTime;


    public LeakyBucketRateLimiter(int limit, double rate) {
        this(limit, rate, 1, TimeUnit.SECONDS);
    }

    public LeakyBucketRateLimiter(int limit, double rate, int perTime, TimeUnit timeUnit) {
        this.limit = limit;
        this.rate = rate;
        this.perTime = perTime;
        this.timeUnit = timeUnit;
        water = 0.0;
        lastLeakTime = System.nanoTime();
    }


    @Override
    public Boolean acquire(long timeout, TimeUnit timeUnit) throws InterruptedException {
        long timeoutNanos = timeUnit.toNanos(timeout);
        long deadline = System.nanoTime() + timeoutNanos;
        try {
            lock.lock();
            while (!tryAcquire() && !Thread.currentThread().isInterrupted()) {
                long now = System.nanoTime();
                if (now >= deadline) {
                    return false;
                }
                //计算生成一个需要的时间，时间/速率
                double singleRequestTime = timeUnit.toNanos(perTime) / rate;
                condition.awaitNanos(Math.min(deadline - now, (long) singleRequestTime));
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
                //计算生成一个需要的时间，时间/速率
                double singleRequestTime = timeUnit.toNanos(perTime) / rate;
                condition.awaitNanos((long) singleRequestTime);
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
            //计算漏了多少水
            double leak = (now - lastLeakTime) * rate / timeUnit.toNanos(perTime);
            water = Math.max(0.0, water - leak);
            lastLeakTime = now;
            if (water + 1 <= limit) {
                water += 1;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
