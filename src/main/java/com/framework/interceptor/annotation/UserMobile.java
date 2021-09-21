package com.framework.interceptor.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface UserMobile {
    
}
