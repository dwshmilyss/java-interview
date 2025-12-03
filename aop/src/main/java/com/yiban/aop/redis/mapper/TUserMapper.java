package com.yiban.aop.redis.mapper;

import com.yiban.aop.redis.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author edz
* @description 针对表【t_user(用户表)】的数据库操作Mapper
* @createDate 2025-12-02 22:03:06
* @Entity com.yiban.aop.redis.entity.TUser
*/
@Mapper
public interface TUserMapper extends BaseMapper<TUser> {
    int insert(TUser tUser);
    TUser selectById(TUser user);
}




