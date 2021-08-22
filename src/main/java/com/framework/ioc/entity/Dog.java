package com.framework.ioc.entity;

import org.springframework.stereotype.Component;

/**
 * @ClassName Dog
 * @Description TODO
 * @Author hxh
 * @Date 2020/12/6 20:08
 * @Version 1.0
 */
@Component

public class Dog implements Pet{
    @Override
    public void move() {
        System.out.println("Running");
    }
}
