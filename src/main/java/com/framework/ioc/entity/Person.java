package com.framework.ioc.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName Person
 * @Description TODO
 * @Author hxh
 * @Date 2020/12/6 20:01
 * @Version 1.0
 */
@Component("person")
public class Person {
    @Value("1")
    private Long id;
    @Value("Jack")
    private String name;

    @Autowired
    private Dog dog;
    @Autowired
    private Bird bird;

    public void callDog(){
        dog.move();
    }
    public void callBird(){
        bird.move();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
