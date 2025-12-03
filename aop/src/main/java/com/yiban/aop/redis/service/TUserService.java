package com.yiban.aop.redis.service;

import com.yiban.aop.redis.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author edz
* @description 针对表【t_user(用户表)】的数据库操作Service
* @createDate 2025-12-02 22:03:06
*/
public interface TUserService extends IService<TUser> {
    int addUser(TUser tUser);

    TUser getUserByID(int id);
}
