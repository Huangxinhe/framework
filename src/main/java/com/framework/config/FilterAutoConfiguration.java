package com.framework.config;

import com.framework.interceptor.annotation.UserId;
import com.framework.interceptor.annotation.UserMobile;
import com.framework.interceptor.handler.UserIdMethodArgumentResolver;
import com.framework.interceptor.handler.UserMobileMethodArgumentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @ClassName FilterAutoConfiguration
 * @Description TODO
 * @Author hxh
 * @Date 9/21/21 6:16 下午
 * @Version 1.0
 */
@Configuration
public class FilterAutoConfiguration {

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass({UserId.class, UserMobile.class})
    protected static class  ArgumentResolverAutoConfiguration extends WebMvcConfigurerAdapter{
        public ArgumentResolverAutoConfiguration() {
        }
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
            argumentResolvers.add(new UserIdMethodArgumentResolver());
            argumentResolvers.add(new UserMobileMethodArgumentResolver());
        }
    }
}
