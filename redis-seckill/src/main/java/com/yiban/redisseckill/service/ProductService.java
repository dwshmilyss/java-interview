package com.yiban.redisseckill.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yiban.redisseckill.bean.Product;
import com.yiban.redisseckill.dao.ProductDao;
import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public static final String PRODUCT_KEY_PREFIX = "product:";
    public static final String LOCK_PRODUCT_HOT_KEY_PREFIX = "lock:hotkey:product:";
    public static final String LOCK_PRODUCT_READWRITE_PREFIX = "lock:readwrite:product:";
    public static final String EMPTY_CACHE = "{}";
    public static AtomicBoolean flag = new AtomicBoolean(false);

    //用Caffeine实现二级缓存
    Cache<String, Product> manualCache = Caffeine.newBuilder()
            .maximumSize(100)
            .build();

    @Resource
    ProductDao productDao;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    Redisson redisson;

    @Transactional
    public Product create(Product product) {
        Product productResult = productDao.create(product);//插入数据库
        manualCache.put(PRODUCT_KEY_PREFIX + productResult.getId(), productResult);//先写入二级缓存
        //写入缓存
        stringRedisTemplate.opsForValue().set(PRODUCT_KEY_PREFIX + productResult.getId(),
                JSONUtil.toJsonStr(productResult), genProductCacheTimeout(), TimeUnit.SECONDS);
        return product;
    }

    @Transactional
    public Product update(Product product) {
        Product productResult;
        RReadWriteLock readwriteLock = redisson.getReadWriteLock(LOCK_PRODUCT_READWRITE_PREFIX + product.getId());
        RLock writeLock = readwriteLock.writeLock();
        writeLock.lock();//这里加写锁，因为是读取DB的，那么在更新缓存的update方法中就要加写锁
        try {
            //这里之所以要把更新DB也放在锁代码块中，是因为MySQL的mvcc会导致读到脏数据
            productResult = productDao.update(product);
            manualCache.put(PRODUCT_KEY_PREFIX + productResult.getId(), productResult);
            //更新缓存
            stringRedisTemplate.opsForValue().set(PRODUCT_KEY_PREFIX + productResult.getId(),
                    JSONUtil.toJsonStr(productResult), genProductCacheTimeout(), TimeUnit.SECONDS);
        } finally {
            writeLock.unlock();
        }
        return productResult;
    }

    public Product get(Long productId) {
        Product product = null;
        product = getProductFromRedis(productId);
        if (product != null) {//如果在缓存中找到就直接返回
            return product;
        }
        //否则就查DB
        //这里为什么防止类似直播带货的突然冒出的热点key，要使用DCL(双重检查锁)，不然热点key在缓存中没有就会导致大量并发请求直接打到DB了，所以接下来要加锁
        // TODO 但是这里还有2个小问题
        // 1. 这里用this会导致多个热点key都阻塞在这里，但是不同的key是可以并发的，比如说多个直播间带货不同的商品，所以这里要对每个商品搞一个对象池，查哪个就用哪个商品对象当锁
        // 2. 这里的对象锁只能锁单机jvm，如果是集群就不行了，所以这里要用分布式锁。
        //synchronized (this) {//单机版
        //分布式锁版本
        RLock productHotKeyLock = redisson.getLock(LOCK_PRODUCT_HOT_KEY_PREFIX + productId);
        boolean locked = false;
        if (flag.compareAndSet(false,true)) {//因为这里是对热点key进行加锁，比如1w个请求并发访问，但是只让第一个过来的请求加锁，后续的请求可以并发访问缓存(因为只有并发访问DB才有压力)，所以这里用一个原子变量来控制一下，只有第一个过来的请求才加锁，后续的请求直接访问缓存就行了
            productHotKeyLock.lock();// 第一个线程真正加锁
            locked = true;
        }
        try {
            //再查一遍缓存
            product = getProductFromRedis(productId);//这里进行第二次查询cache，因为加了同步锁，所以即使是热点key，也只会有一个请求进来，查询了DB之后就会更新到缓存中，所以后续并发请求这个key的查询就直接从缓存中获取了
            if (product != null) {
                return product;
            }
            //TODO 性能优化1 为了防止缓存和数据库不一致的问题，这里也要加锁，但是为了优化性能，这里要加读写锁
            RReadWriteLock readwriteLock = redisson.getReadWriteLock(LOCK_PRODUCT_READWRITE_PREFIX + productId);
            RLock readLock = readwriteLock.readLock();
            readLock.lock();//这里加读锁，因为是读取DB的，那么在更新缓存的update方法中就要加写锁
            try {
                product = productDao.findById(productId);
                if (product != null) {
                    manualCache.put(PRODUCT_KEY_PREFIX + product.getId(), product);
                    stringRedisTemplate.opsForValue().set(PRODUCT_KEY_PREFIX + product.getId(),
                            JSONUtil.toJsonStr(product), genProductCacheTimeout(), TimeUnit.SECONDS);
                } else {//防止缓存穿透，因为缓存中没有，DB中也没有
                    manualCache.put(PRODUCT_KEY_PREFIX + product.getId(), null);
                    stringRedisTemplate.opsForValue().set(PRODUCT_KEY_PREFIX + product.getId(),
                            EMPTY_CACHE, genEmptyCacheTimeout(), TimeUnit.SECONDS);
                }
            } finally {
                readLock.unlock();
            }
        } finally {
            if (locked) {
                productHotKeyLock.unlock();
            }
        }
        return product;

    }

    @Nullable
    private Product getProductFromRedis(Long productId) {
        Product product;
        String productJson = stringRedisTemplate.opsForValue().get(PRODUCT_KEY_PREFIX + productId);
        if (productJson.equals(EMPTY_CACHE)) {//防止缓存穿透，因为缓存中没有，DB中也没有
            //但是也不能让缓存一直存在，因为有可能有黑客攻击伪造了大量不存在的商品ID，如果都放在Redis中会占用大量内存，所以也要设置一个过期时间
            stringRedisTemplate.expire("product:" + productId, genEmptyCacheTimeout(), TimeUnit.SECONDS);
            return null;
        }
        product = JSONUtil.toBean(productJson, Product.class);
        // 判断一下该key的过期时间，如果过期时间小于某个值的话，就同步更新一下缓存TTL，防止缓存击穿
        Long expireTime = stringRedisTemplate.getExpire(PRODUCT_KEY_PREFIX + productId, TimeUnit.SECONDS);
        if (expireTime != null && expireTime < 10 * 60) { // 小于10分钟
            // 同步更新缓存TTL
            stringRedisTemplate.expire("product:" + productId, genProductCacheTimeout(), TimeUnit.SECONDS);
        }
        return product;
    }

    //解决缓存雪崩,(运营人员一次性导入大量的商品，如果把过期时间设置一样的话，那同一时间就会有大量的缓存失效，导致雪崩)
    private int genProductCacheTimeout() {
        return PRODUCT_CACHE_TIMEOUT + RandomUtil.randomInt(30) * 60;
    }

    private int genEmptyCacheTimeout() {
        return RandomUtil.randomInt(30) * 60;
    }

}
