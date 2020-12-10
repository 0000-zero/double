package com.at.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.at.dubbo.bean.UserAddress;
import com.at.dubbo.service.OrderServcie;
import com.at.dubbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zero
 * @create 2020-12-10 18:14
 */
@Service
public class OrderServcieImpl implements OrderServcie {

    @Autowired
//    @Reference(check = false)  //拉取远程bean并给userservice注入
    UserService userService;


    @Override
    public void initOrder(String userId) {
        List<UserAddress> userAddresses = userService.getUserAddressList("1");
        System.err.println(userAddresses);
    }
}
