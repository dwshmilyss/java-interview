package com.yiban.springbootdemo.mapper;

import com.yiban.springbootdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int insertUser(User user);
    User selectByUsername(@Param("username") String username);
    List<User> selectAll();
}
