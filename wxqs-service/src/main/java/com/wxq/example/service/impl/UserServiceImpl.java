package com.wxq.example.service.impl;

import com.wxq.example.dao.UserMapper;
import com.wxq.example.model.User;
import com.wxq.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2017/9/25.
 */
@Service
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Override
    public User getUserById(Integer id) {
        log.info("数据库地址---------"+jdbcUrl);
        return userMapper.getById(id);
    }

    @Override
    public User getTransaction() {
        User user = new User();
        user.setName("丹丹");
        user.setSex((byte)1);
        User user1 = new User();
        user1.setName("丽丽");
        user1.setSex((byte)0);
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        userMapper.batchInsert(list);
        User user2 = userMapper.getById(100);
        String name = user2.getName();
        return user;
    }
}
