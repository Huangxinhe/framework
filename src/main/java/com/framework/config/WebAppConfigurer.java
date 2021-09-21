package com.framework.config;

import com.framework.interceptor.filter.TestFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebAppConfigurer
 * @Description TODO
 * @Author hxh
 * @Date 9/21/21 5:26 下午
 * @Version 1.0
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TestFilter()).addPathPatterns("/**");
    }
}
