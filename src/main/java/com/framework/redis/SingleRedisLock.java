package com.framework.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @ClassName SingleRedisLock
 * @Description TODO
 * @Author hxh
 * @Date 8/22/21 10:50 下午
 * @Version 1.0
 */
public class SingleRedisLock {
    JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);

    //锁过期时间
    protected long internalLockLeaseTime = 30000;

    //获取锁的超时时间
    private final long timeout = 999999;

    /**
     * 加锁
     *
     * @param lockKey 锁键
     * @param requestId 请求唯一标识
     * @return
     */
    SetParams setParams = SetParams.setParams().nx().px(internalLockLeaseTime);

    public boolean tryLock(String lockKey, String requestId) {

        String threadName = Thread.currentThread().getName();

        Jedis jedis = this.jedisPool.getResource();

        long start = System.currentTimeMillis();

        try {
            for (; ; ) {
                String lockResult = jedis.set(lockKey, requestId, setParams);
                if ("OK".equals(lockResult)) {
                    System.out.println(threadName + ":   获取锁成功");
                    return true;
                }
                //否则循环等待，在timeout时间内仍未获取到锁，则获取失败
                System.out.println(threadName + ":   获取锁失败，等待中");
                long l = System.currentTimeMillis() - start;
                //获取锁的超时时间
                if (l >= timeout) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            jedis.close();
        }

    }

    /**
     * 解锁
     *
     * @param lockKey   锁键
     * @param requestId 请求唯一标识
     *                  判断与删除分成两步执行，则无法保证原子性，一样会出现问题。所以解锁时不仅要保证加锁和解锁是同
     *                  一个人还要保证解锁的原子性。因此结合lua脚本完成查询&删除操作
     * @return
     */
    public boolean releaseLock(String lockKey, String requestId) {

        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + "：释放锁");
        Jedis jedis = this.jedisPool.getResource();

        String lua =
                "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                        "   return redis.call('del',KEYS[1]) " +
                        "else" +
                        "   return 0 " +
                        "end";

        try {
            Object result = jedis.eval(lua, Collections.singletonList(lockKey),
                    Collections.singletonList(requestId));
            if ("1".equals(result.toString())) {
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }

    }
}
