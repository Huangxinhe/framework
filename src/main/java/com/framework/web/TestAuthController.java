package com.framework.web;

import com.framework.interceptor.annotation.UserAuthenticate;
import com.framework.interceptor.annotation.UserId;
import com.framework.interceptor.annotation.UserMobile;
import com.framework.response.BaseResultMsg;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestAuthController
 * @Description TODO
 * @Author hxh
 * @Date 9/21/21 6:20 下午
 * @Version 1.0
 */
@Validated
@RestController
public class TestAuthController {

    @UserAuthenticate
    @GetMapping("/testAuth")
    public BaseResultMsg testAuth(@UserId Long userId, @UserMobile String userMobile) {
        System.out.println("userId:" + userId + " userMobile:" + userMobile);
        return BaseResultMsg.ofSuccess();
    }
}
