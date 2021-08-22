package com.framework.aop;

/**
 * @ClassName RealPayment
 * @Description TODO
 * @Author hxh
 * @Date 2020/12/6 22:02
 * @Version 1.0
 */
public class RealPayment implements Payment{
    @Override
    public void pay() {
        System.out.println("作为用户只关心支付功能");
    }
}
