package com.at.dubbo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zero
 * @create 2020-12-10 18:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress implements Serializable {

    private Integer id;
    private String userAddress;
    private String userId;
    private String consignee;
    private String phoneNum;
    private String isDefault;

}
