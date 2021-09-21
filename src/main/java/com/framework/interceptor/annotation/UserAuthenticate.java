package com.framework.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UserAuthenticate {
    /**
     * 是否需要检验访问权限，默认不校验
     * 这是预留给咱们后台管理系统的，后台管理员角色不同访问权限不同，所以将这个设置为True
     * @return
     */
    boolean permission() default false;
}
