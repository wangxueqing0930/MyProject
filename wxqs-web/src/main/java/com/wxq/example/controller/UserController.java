package com.wxq.example.controller;

import com.wxq.example.model.User;
import com.wxq.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/9/29.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public User test(@RequestParam(value = "id") Integer id){
        User user = userService.getUserById(id);
        return user;
    }

    @RequestMapping(value = "/testTransaction",method = RequestMethod.GET)
    @ResponseBody
    public User testTransaction(){
        User user = userService.getTransaction();
        return user;
    }
}
