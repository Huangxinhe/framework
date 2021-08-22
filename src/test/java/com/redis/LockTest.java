package com.redis;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LockTest
 * @Description TODO
 * @Author hxh
 * @Date 8/22/21 11:14 下午
 * @Version 1.0
 */
public class LockTest {
    public static void main(String[] args) {
        //模拟多个客户端
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new LockRunnable());
            thread.start();
        }
    }

    private static class LockRunnable implements Runnable{
        @Override
        public void run(){
            SingleRedisLock singleRedisLock = new SingleRedisLock();

            String requestId = UUID.randomUUID().toString();

            boolean lockResult = singleRedisLock.tryLock("lock",requestId);
            if (lockResult){
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            singleRedisLock.releaseLock("lock",requestId);
        }
    }
}
