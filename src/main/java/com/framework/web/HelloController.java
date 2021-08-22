package com.framework.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author hxh
 * @Date 2020/11/23 17:23
 * @Version 1.0
 */
@RestController
public class HelloController {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    @ResponseBody
    public String hello(){
        String sentence = "Hello World";
        System.out.println(sentence);
        return sentence;
    }
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    @ResponseBody
    public String hi(){
        String sentence = "Hi World";
        System.out.println(sentence);
        return sentence;
    }

}
