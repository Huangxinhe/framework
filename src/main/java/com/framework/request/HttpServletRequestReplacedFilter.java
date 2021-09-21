package com.framework.request;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName HttpServletRequestReplacedFilter
 * @Description TODO
 * @Author hxh
 * @Date 9/21/21 9:25 下午
 * @Version 1.0
 */
public class HttpServletRequestReplacedFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest){
            requestWrapper = new RequestReaderHttpServletRequestWrapper((HttpServletRequest) request);
        }
        //获取请求中的流如何，将取出来的字符串，再次转换成流，然后把它放入到新request对象中
        //在chain.doFilter方法中传递新的request对象
        if (requestWrapper == null){
            filterChain.doFilter(request,response);
        }else {
            filterChain.doFilter(requestWrapper,response);
        }
    }

    @Override
    public void destroy() {

    }
}
