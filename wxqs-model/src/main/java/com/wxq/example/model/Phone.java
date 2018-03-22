package com.wxq.example.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/29.
 */
public class Phone implements Serializable{
    private Long id;
    private Long userId;
    private String phoneNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
