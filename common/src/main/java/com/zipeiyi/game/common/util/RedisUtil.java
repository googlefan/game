package com.zipeiyi.game.common.util;

import com.zipeiyi.core.service.IdCenterService;
import com.zipeiyi.xpower.cache.IRedis;
import com.zipeiyi.xpower.cache.impl.RedisAutoConfigCacheFactory;

/**
 * Created by zhangxiaoqiang on 16/12/6.
 */
public class RedisUtil {

    private static RedisAutoConfigCacheFactory redisAutoConfigCacheFactory = RedisAutoConfigCacheFactory.getInstance();
    public static IRedis getRedis(){return redisAutoConfigCacheFactory.getCache("redis.game");}

}
