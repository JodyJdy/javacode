package ratelimiter;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        testFixedWindowRateLimiter();
//        testSlidingWindowRateLimiter();
//        testLeakyBucketRateLimiter();
//        testTokenBucketRateLimiter();

    }

    public static void testFixedWindowRateLimiter() throws InterruptedException {
        final RateLimiter limiter = new FixedWindowRateLimiter(20, 4, TimeUnit.SECONDS);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    limiter.acquire();
                    System.out.println("thread1");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    limiter.acquire();
                    System.out.println("thread2");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
    public static void testSlidingWindowRateLimiter() throws InterruptedException {
        final RateLimiter limiter = new FixedWindowRateLimiter(5, 1, TimeUnit.SECONDS);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    limiter.acquire();
                    System.out.println("thread1");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    limiter.acquire();
                    System.out.println("thread2");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
    public static void testLeakyBucketRateLimiter() throws InterruptedException {
        //  100 ms 生成 1个
        final RateLimiter limiter = new LeakyBucketRateLimiter(5,1.0,100,TimeUnit.MILLISECONDS);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    limiter.acquire();
                    System.out.println("thread1");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    limiter.acquire();
                    System.out.println("thread2");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    public static void testTokenBucketRateLimiter() throws InterruptedException {
        //  100 ms 生成 1个
        final RateLimiter limiter = new TokenBucketRateLimiter(5,1.0,100,TimeUnit.MILLISECONDS);

        Thread.sleep(5000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    limiter.acquire();
                    System.out.println("thread1");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    limiter.acquire();
                    System.out.println("thread2");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
