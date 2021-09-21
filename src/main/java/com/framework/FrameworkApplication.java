package com.framework;

import com.framework.ioc.entity.Person;
import com.framework.request.HttpServletRequestReplacedFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FrameworkApplication {

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName","paramValue");
        registration.setOrder(1);
        return registration;
    }

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(FrameworkApplication.class, args);
//        Person person = (Person) ctx.getBean("person");
//        System.out.println(person.getId()+"-"+person.getName());
//        person.callDog();
//        person.callBird();

    }

}
