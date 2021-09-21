package com.framework.interceptor.handler;

import com.framework.interceptor.HeaderCons;
import com.framework.interceptor.annotation.UserMobile;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName UserIdMethodArgumentResolver
 * @Description TODO
 * @Author hxh
 * @Date 9/21/21 6:11 下午
 * @Version 1.0
 */
public class UserMobileMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(UserMobile.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        
        return servletRequest.getAttribute(HeaderCons.USER_MOBILE);
    }
}
