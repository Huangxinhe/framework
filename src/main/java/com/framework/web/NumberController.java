package com.framework.web;

import com.redis.JedisLock;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class NumberController {

    private AtomicInteger count = new AtomicInteger(0);

    @Resource
    private Jedis jedis;

    @PutMapping("/number")
    public synchronized void subNumber() throws InterruptedException {
        Integer number = Integer.parseInt(jedis.get("number"));

//        Thread.sleep(400);
        number-=1;
        jedis.set("number" , number.toString());
        count.addAndGet(1);
        System.out.println(count.get());
    }

    @PutMapping("/subNumberDistribute")
    public void subNumberDistribute() throws InterruptedException {
        String requestId = UUID.randomUUID().toString();
        String LOCKKEY = "LOCKKEY";
        if(JedisLock.tryGetDistributedLock(jedis , LOCKKEY , requestId , 3000)){
            Integer number = Integer.parseInt(jedis.get("number"));
            Thread.sleep(400);
            number-=1;
            jedis.set("number" , number.toString());
            count.addAndGet(1);
            System.out.println(count.get());
            JedisLock.releaseDistributedLock(jedis , LOCKKEY , requestId);
        }
    }
}
