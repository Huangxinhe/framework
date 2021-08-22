package com.framework.aop;

/**
 * @ClassName Alipay
 * @Description TODO
 * @Author hxh
 * @Date 2020/12/6 22:03
 * @Version 1.0
 */
public class Alipay implements Payment{

    Payment payment;

    public Alipay(Payment payment) {
        this.payment = payment;
    }
    public void beforePay(){
        System.out.println("从招行取钱");
    }

    @Override
    public void pay() {
        beforePay();
        payment.pay();
        afterPay();
    }

    public void afterPay(){
        System.out.println("支付给慕课网");
    }

}
