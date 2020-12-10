package com.at.dubbo.service;

import com.at.dubbo.bean.UserAddress;

import java.util.List;

/**
 * @author zero
 * @create 2020-12-10 18:08
 */
public interface UserService {

    /**
     * 获取哦一农户的地址列表
     * @param userId
     * @return
     */
    List<UserAddress> getUserAddressList(String userId);

}
