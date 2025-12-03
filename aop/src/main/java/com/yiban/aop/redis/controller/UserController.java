package com.yiban.aop.redis.controller;

import com.yiban.aop.redis.entity.TUser;
import com.yiban.aop.redis.service.TUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author david.duan
 * @packageName com.yiban.aop.redis.controller
 * @className UserController
 * @date 2025/12/2
 * @description
 */
@RestController
@Slf4j
public class UserController {
    @Resource
    TUserService tUserService;

    @PostMapping("/user/add")
    public int addUser(@RequestBody TUser tUser) {
        return tUserService.addUser(tUser);
    }

    @GetMapping("/user/get/{id}")
    public TUser getUserByID(@PathVariable("id") int id) {
        return tUserService.getUserByID(id);
    }
}
