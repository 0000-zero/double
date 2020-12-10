package com.at.dubbo;

import com.at.dubbo.service.OrderServcie;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author zero
 * @create 2020-12-10 18:39
 */
public class OrderMainApplication {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
        while (true){
            try { Thread.sleep(300); } catch (InterruptedException e) {e.printStackTrace();}
            OrderServcie orderServcie = context.getBean(OrderServcie.class);
            orderServcie.initOrder("1");
        }
//        System.in.read();
    }

}
