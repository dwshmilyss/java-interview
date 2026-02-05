package com.yiban.redisseckill.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONParser;
import cn.hutool.json.JSONUtil;
import com.yiban.redisseckill.bean.Product;
import com.yiban.redisseckill.dao.ProductDao;
import com.yiban.redisseckill.mapper.ProductMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.redisseckill.service
 * @className ProductService
 * @date 2026/2/4
 * @description
 */
@Service
public class ProductService {
    public static final int PRODUCT_CACHE_TIMEOUT = 60 * 60 * 24;//24小时
    public static final String EMPTY_CACHE = "{}";

    @Resource
    ProductDao productDao;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Transactional
    public Product create(Product product) {
        Product productResult = productDao.create(product);//插入数据库
        //写入缓存
        stringRedisTemplate.opsForValue().set("product:" + productResult.getId(),
                JSONUtil.toJsonStr(productResult), genProductCacheTimeout(), TimeUnit.SECONDS);
        return product;
    }

    @Transactional
    public Product update(Product product) {
        Product productResult = productDao.update(product);
        //更新缓存
        stringRedisTemplate.opsForValue().set("product:" + productResult.getId(),
                JSONUtil.toJsonStr(productResult), genProductCacheTimeout(), TimeUnit.SECONDS);
        return productResult;
    }

    public Product get(Long productId) {
        String productJson = stringRedisTemplate.opsForValue().get("product:" + productId);
        if (productJson != null) {
            if (productJson.equals(EMPTY_CACHE)) {//防止缓存穿透，因为缓存中没有，DB中也没有
                return null; // 缓存空值，表示数据库中也无此数据
            }
            Product product = JSONUtil.toBean(productJson, Product.class);
            // 判断一下该key的过期时间，如果过期时间小于某个值的话，就同步更新一下缓存TTL，防止缓存击穿
            Long expireTime = stringRedisTemplate.getExpire("product:" + productId, TimeUnit.SECONDS);
            if (expireTime != null && expireTime < 10 * 60) { // 小于10分钟
                // 同步更新缓存TTL
                stringRedisTemplate.expire("product:" + productId, genProductCacheTimeout(), TimeUnit.SECONDS);
            }
            return product;
        } else {
            Product product = productDao.findById(productId);
            if (product != null) {
                stringRedisTemplate.opsForValue().set("product:" + product.getId(),
                        JSONUtil.toJsonStr(product), genProductCacheTimeout(), TimeUnit.SECONDS);
            } else {//防止缓存穿透，因为缓存中没有，DB中也没有
                stringRedisTemplate.opsForValue().set("product:" + product.getId(),
                        EMPTY_CACHE, genEmptyCacheTimeout(), TimeUnit.SECONDS);
            }
            return product;
        }
    }

    //解决缓存雪崩,(运营人员一次性导入大量的商品，如果把过期时间设置一样的话，那同一时间就会有大量的缓存失效，导致雪崩)
    private int genProductCacheTimeout() {
        return PRODUCT_CACHE_TIMEOUT + RandomUtil.randomInt(30) * 60;
    }

    private int genEmptyCacheTimeout() {
        return 60 + RandomUtil.randomInt(30) * 60;
    }

}
