package com.yiban.redisseckill.dao;

import com.yiban.redisseckill.bean.Product;
import com.yiban.redisseckill.mapper.ProductMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

/**
 * @author david.duan
 * @packageName com.yiban.redisseckill.dao
 * @className ProductDao
 * @date 2026/2/4
 * @description
 */
@Repository
public class ProductDao {
    @Resource
    ProductMapper productMapper;

    public Product create(Product product) {
        int res = productMapper.insert(product);
        if (res > 0) {
            return productMapper.selectById(product.getId());
        } else {
            throw new RuntimeException("创建商品失败");
        }
    }

    public Product update(Product product) {
        int res = productMapper.update(product);
        if (res > 0) {
            return productMapper.selectById(product.getId());
        } else {
            throw new RuntimeException("更新商品失败");
        }
    }

    public Product findById(Long id) {
        return productMapper.selectById(id);
    }
}
