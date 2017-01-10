package com.game.db.redis;

import com.zipeiyi.xpower.cache.IRedis;
import com.zipeiyi.xpower.cache.impl.RedisAutoConfigCacheFactory;

public class RedisService {

	private static RedisService instance ;
	
	RedisAutoConfigCacheFactory redisAutoConfigCacheFactory = RedisAutoConfigCacheFactory.getInstance();

	public static RedisService getInstance(){
		if(instance == null){
			instance = new RedisService();
		}
		return instance;
	}
    public IRedis getRedis() {
        return redisAutoConfigCacheFactory.getCache("redis.game");
    }
    
    
}
