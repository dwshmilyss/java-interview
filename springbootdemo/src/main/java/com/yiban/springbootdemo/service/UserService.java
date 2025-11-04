package com.yiban.springbootdemo.service;

import com.yiban.springbootdemo.entity.User;

import java.util.List;


public interface UserService {
    void addUser(User user);
    User getByUsername(String username);
    List<User> getAll();
}
