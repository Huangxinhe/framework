package com.framework.interceptor.filter;


import com.framework.interceptor.HeaderCons;
import com.framework.interceptor.annotation.UserAuthenticate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @ClassName TestFilter
 * @Description TODO
 * @Author hxh
 * @Date 9/21/21 5:12 下午
 * @Version 1.0
 */
public class TestFilter extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(TestFilter.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        logger.info("request 请求地址path[{}] uri[{}]", request.getServletPath(), request.getRequestURI());
        //request.getHeader(String) 从请求头中获取数据
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        UserAuthenticate userAuthenticate = method.getAnnotation(UserAuthenticate.class);
        //如果没有加注解 userAuthenticate为null
        if (Objects.nonNull(userAuthenticate)){
            Long userId = getUsserId(request);
            if (null != userId && checkAuth(userId, request.getRequestURI())) {
                return true;
            }
        }
        //这里的异常是我自定义的异常，系统抛出异常后框架捕获，然后转为统一的格式返回给前端
        throw new RuntimeException("No access");
    }

    /**
     * 根据token获取用户Id
     *
     * @param request
     * @return
     */
    private Long getUsserId(HttpServletRequest request) {
        //添加业务逻辑根据token获取用户UserId
        request.getHeader(HeaderCons.USER_ID);
        Long userId = 1L;
        String userMobile = "18888888888";
        request.setAttribute(HeaderCons.USER_ID,userId);
        request.setAttribute(HeaderCons.USER_MOBILE,userMobile);
        return userId;
    }

    private boolean checkAuth(Long userId, String requestURI) {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}
