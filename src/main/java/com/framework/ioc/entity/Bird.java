package com.framework.ioc.entity;

import org.springframework.stereotype.Component;

/**
 * @ClassName Bird
 * @Description TODO
 * @Author hxh
 * @Date 2020/12/6 20:15
 * @Version 1.0
 */
@Component
//@Primary
public class Bird implements Pet{
    @Override
    public void move() {
        System.out.println("Flying");
    }
}
