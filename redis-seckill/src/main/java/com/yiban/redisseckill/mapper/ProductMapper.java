package com.yiban.redisseckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiban.redisseckill.bean.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author david.duan
 * @packageName com.yiban.redisseckill.mapper
 * @interfaceName ProductMapper
 * @date 2026/2/4
 * @description
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 插入商品
     * @param product 商品对象
     * @return 插入后的商品对象（包含自增ID）
     */
     int insert(Product product);

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品对象
     */
    Product selectById(Long id);

    /**
     * 更新商品信息
     * @param product 商品对象
     * @return 更新行数
     */
    int update(Product product);

    /**
     * 删除商品
     * @param id 商品ID
     * @return 删除行数
     */
    int deleteById(Long id);
}
