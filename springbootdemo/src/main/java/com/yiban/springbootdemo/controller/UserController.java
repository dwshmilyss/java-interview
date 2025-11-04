package com.yiban.springbootdemo.controller;

import com.yiban.springbootdemo.entity.User;
import com.yiban.springbootdemo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Indexed
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 插入用户
    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        userService.addUser(user);
        return "Insert success!";
    }

    // 按用户名查询
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    // 查询所有
    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAll();
    }

}
