package com.framework;

import com.framework.ioc.entity.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FrameworkApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(FrameworkApplication.class, args);
//        Person person = (Person) ctx.getBean("person");
//        System.out.println(person.getId()+"-"+person.getName());
//        person.callDog();
//        person.callBird();
    }

}
