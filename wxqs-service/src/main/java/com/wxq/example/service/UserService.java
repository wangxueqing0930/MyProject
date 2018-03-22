package com.wxq.example.service;

import com.wxq.example.model.User;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/9/25.
 */
public interface UserService {
    User getUserById(Integer id);

    User getTransaction();
}
