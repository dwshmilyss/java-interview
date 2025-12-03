package com.yiban.aop.redis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiban.aop.redis.entity.TUser;
import com.yiban.aop.redis.service.TUserService;
import com.yiban.aop.redis.mapper.TUserMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
* @author edz
* @description 针对表【t_user(用户表)】的数据库操作Service实现
* @createDate 2025-12-02 22:03:06
*/
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>
    implements TUserService{

    public static final String CACHE_KEY_USER = "user_";

    @Resource
    TUserMapper tUserMapper;

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public int addUser(TUser tUser) {
        //1.先插入mysql
        int returnVal = tUserMapper.insert(tUser);
        //2.再插入redis
        if (returnVal > 0) {
            //2.1 插入redis之前先从MySQL把数据查出来，保证数据一致性
            tUser = tUserMapper.selectById(tUser.getId());
            //2.2 拼接key
            String key = CACHE_KEY_USER + tUser.getId();
            //2.3 插入redis
            redisTemplate.opsForValue().set(key,tUser);
        }
        return returnVal;
    }

    @Override
    public TUser getUserByID(int id) {
        TUser tUser = null;
        //1.先从redis查
        String key = CACHE_KEY_USER + id;
        tUser = (TUser) redisTemplate.opsForValue().get(key);
        //2.如果redis没有，再从mysql查
        if (tUser == null) {
            tUser = tUserMapper.selectById(id);
            //3.查到后放入redis
            if (tUser != null) {
                redisTemplate.opsForValue().set(key,tUser);
            }
        }
        //3.redis有，直接返回
        return tUser;
    }
}




