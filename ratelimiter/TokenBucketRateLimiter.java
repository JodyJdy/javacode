package ratelimiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *令牌桶算法
 */
public class TokenBucketRateLimiter implements RateLimiter {
    /**
     * 令牌桶容量
     */
    private final int limit;

    /**
     * 令牌桶生成速率
     */
    private final double rate;
    /**
     * 每 perTime 时间，令牌生成 rate
     */
    private final int perTime;
    /**
     * 令牌生成时间单位
     */
    private final TimeUnit timeUnit;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    /**
     * 令牌桶容量
     */
    private double tokens;

    /**
     * 上次生成令牌时间
     */
    private long lastGenerateTime;


    public TokenBucketRateLimiter(int limit, double rate) {
        this(limit, rate, 1, TimeUnit.SECONDS);
    }

    public TokenBucketRateLimiter(int limit, double rate, int perTime, TimeUnit timeUnit) {
        this.limit = limit;
        this.rate = rate;
        this.perTime = perTime;
        this.timeUnit = timeUnit;
        tokens = 0;
        lastGenerateTime = System.nanoTime();
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
            //计算生成了多少令牌
            double generate = (now - lastGenerateTime) * rate / timeUnit.toNanos(perTime);

            tokens = Math.min(limit, tokens + generate);

            if (tokens >= 1) {
                tokens -= 1;
                lastGenerateTime = now;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
