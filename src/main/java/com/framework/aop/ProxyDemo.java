package com.framework.aop;

/**
 * @ClassName ProxyDemo
 * @Description TODO
 * @Author hxh
 * @Date 2020/12/6 22:05
 * @Version 1.0
 */
public class ProxyDemo {
    public static void main(String[] args) {
        Payment proxy = new Alipay(new RealPayment());
        proxy.pay();
    }
}
