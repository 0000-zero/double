package com.at.dubbo;

import com.at.dubbo.bean.UserAddress;
import com.at.dubbo.service.UserService;

import java.util.List;

/**
 * @author zero
 * @create 2020-12-10 20:20
 */
public class UserServiceStub implements UserService {

    //本地存根


    /**
     * 在consumer中配置stub后会默认给userservice注入值
     */
    private final UserService userService;

    public UserServiceStub(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<UserAddress> getUserAddressList(String userId) {

        System.out.println("调用本地存根。。。。。。");

        if(userId != null && userId.length()>0){
            userService.getUserAddressList("1");
        }


        return null;
    }
}
