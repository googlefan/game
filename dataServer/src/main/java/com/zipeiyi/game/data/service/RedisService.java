package com.zipeiyi.game.data.service;


import com.zipeiyi.xpower.cache.IRedis;
import com.zipeiyi.xpower.cache.impl.RedisAutoConfigCacheFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zhuhui on 16-12-8.
 */
@Service
public class RedisService {
    RedisAutoConfigCacheFactory redisAutoConfigCacheFactory = RedisAutoConfigCacheFactory.getInstance();

    IRedis getRedis() {
        return redisAutoConfigCacheFactory.getCache("redis.game");
    }

}
