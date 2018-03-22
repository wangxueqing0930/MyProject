package com.wxq.example.dao;

import com.wxq.example.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/9/29.
 */
public interface UserMapper {
    User getById(Integer id);

    int batchInsert(@Param("list") List<User> list);
}
