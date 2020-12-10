package com.at.dubbo.service.impl;

import com.at.dubbo.bean.UserAddress;
import com.at.dubbo.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * @author zero
 * @create 2020-12-10 18:10
 */
//@Service
public class UserServiceImpl1 implements UserService {
    @Override
    public List<UserAddress> getUserAddressList(String userId) {

        System.out.println("provider .... 1");

//        try { Thread.sleep(5000); } catch (InterruptedException e) {e.printStackTrace();}

        UserAddress userAddress = new UserAddress(1, "上海松江", "1", "mm", "15600000000", "1");
        UserAddress userAddress1 = new UserAddress(2, "上海松江", "2", "hh", "15600000001", "1");
        return Arrays.asList(userAddress, userAddress1);
    }
}
