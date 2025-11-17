package ratelimiter;

import java.util.concurrent.TimeUnit;

/**
 * 限流接口
 */
public interface RateLimiter {
    /**
     *带有超时参数的获取令牌方法
     * @param timeout
     * @return
     * @throws InterruptedException
     */
    public Boolean acquire(long timeout, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 阻塞获取令牌方法
     * @return
     * @throws InterruptedException
     */
    public Boolean acquire() throws InterruptedException;

    /**
     *立即获取令牌方法
     *
     */
    public Boolean tryAcquire();
}
