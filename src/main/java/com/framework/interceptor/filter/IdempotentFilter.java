package com.framework.interceptor.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.framework.SimpleLock;
import com.framework.interceptor.annotation.Idempotent;
import com.framework.request.HttpHelper;
import com.framework.request.RequestReaderHttpServletRequestWrapper;
import com.framework.utils.SpringContextUtil;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @ClassName IdempotentFilter
 * @Description TODO
 * @Author hxh
 * @Date 9/21/21 10:01 下午
 * @Version 1.0
 */
public class IdempotentFilter extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(IdempotentFilter.class);
    private static final String IDEMPOTENT = "idempotent.info";
    private static final String NAMESPACE = "idempotent";
    private static final String NAMESPACE_LOCK = "idempotent.lock";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("request 请求地址path[{}] uri[{}]",request.getServletPath(),request.getRequestURI());
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Idempotent ra = method.getAnnotation(Idempotent.class);
        if (Objects.nonNull(ra)){
            logger.debug("start doIdempotent");
            int liveTime = getIdempotentLOckExpiredTime(ra);
            String key = generateKey(request,ra);
            logger.debug("Finish generateKey:[{}]",key);
            JedisCluster jedisCluster = getJedisCluster();
            //上分布式锁 避免相同的请求同时进入调用jedisCluster.get(key) 都为null的情况
            new SimpleLock(NAMESPACE_LOCK+key,jedisCluster).wrap(new Runnable(){

                @Override
                public void run() {
                    //判断key是否存在，如存在则抛出重复提交异常，如果不存在 则新增
                    if (jedisCluster.get(key) == null){
                        jedisCluster.setex(key,liveTime,"true");
                        request.setAttribute(IDEMPOTENT,key);
                    }else {
                        logger.debug("the key exist: {}, will be expored after {} mils if not be cleared",key,liveTime);
                        throw new RuntimeException("请勿重复提交");
                    }
                }
            });
        }
        return true;
    }

    private int getIdempotentLOckExpiredTime(Idempotent ra){
        return ra.expiredTime();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{

    }

    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) throws Exception{
        try {
            afterIdempotent(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void afterIdempotent(HttpServletRequest request){
        Object obj = request.getAttribute(IDEMPOTENT);
        if (obj!=null){
            logger.debug("Start afterIdempotent");
            String key = obj.toString();
            JedisCluster jedisCluster = getJedisCluster();
            if (StringUtils.isNotBlank(key)&&jedisCluster.del(key) == 0){
                logger.debug("afterIdempotent error Prepared to delete the key:[{}]",key);
            }
            logger.debug("End afterIdempotent");
        }
    }

    public String generateKey(HttpServletRequest request,Idempotent ra){
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        StringBuilder result = new StringBuilder(NAMESPACE);
        String token = request.getHeader("H-User-Token");
        append(result,requestURI);
        append(result,requestMethod);
        append(result,token);
        appendBodyData(request,result,ra);
        logger.debug("The raw data to be generated key:{}",result.toString());
        return DigestUtils.sha1DigestAsHex(result.toString());
    }

    private void appendBodyData(HttpServletRequest request,StringBuilder src,Idempotent ra){
        if (Objects.nonNull(ra)){
            boolean shouldHashBody = ra.body();
            logger.debug("Found attr for body in @Idempotent,the value id {}" ,shouldHashBody);
            if (shouldHashBody){
                String data = null;
                try {
                    data = HttpHelper.getBodyString(new RequestReaderHttpServletRequestWrapper(request));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (StringUtils.isBlank(data)){
                    logger.warn("Found attr for body in @Idempotent,but the body is blank");
                    return;
                }
                String[] bodyVals = ra.bodyVals();
                //bodyVals优先
                if (Objects.nonNull(bodyVals)&&bodyVals.length!=0){
                    logger.debug("Found attr for @Idempotent ,the values is {}", Arrays.asList(bodyVals));
                    final String finalData = data;
                    Arrays.asList(bodyVals).stream().sorted().forEach(e->{
                        String val = getEscapedVal(finalData,e);
                        append(src,val);
                    });
                }else {
                    append(src,data);
                }
            }
        }
    }

    private String getEscapedVal(String json,String path){
        String[] paths = path.split(":");
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String nodeVal = json;
        for(String fieldName:paths){
            if(isInteger(fieldName)){
                try {
                    jsonArray = JSONObject.parseArray(nodeVal);
                    nodeVal = jsonArray.get(Integer.parseInt(fieldName)).toString();
                } catch (JSONException e) {
                    jsonObject = JSONObject.parseObject(nodeVal);
                    nodeVal = jsonObject.get(fieldName).toString();
                }
            }else {
                jsonObject = JSONObject.parseObject(nodeVal);
                nodeVal = jsonObject.get(fieldName).toString();
            }
        }
        return nodeVal;
    }

    public static boolean isInteger(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private void append(StringBuilder src,String str){
        if (!StringUtils.isBlank(str)){
            src.append("#").append(str);
        }
    }

    //手动注入
    public JedisCluster getJedisCluster(){
        return SpringContextUtil.getBean(JedisCluster.class);
    }


}
