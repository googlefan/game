package com.zipeiyi.game.gateServer.service.antispam.impl;

import com.zipeiyi.game.gateServer.service.antispam.Frequency;
import com.zipeiyi.xpower.cache.IRedis;
import com.zipeiyi.xpower.cache.impl.RedisAutoConfigCacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangxiaoqiang on 16/12/5.
 */
public class FrequencyImpl implements Frequency {

    private static final Logger logger = LoggerFactory.getLogger(FrequencyImpl.class);

    RedisAutoConfigCacheFactory redisAutoConfigCacheFactory = RedisAutoConfigCacheFactory.getInstance();

    IRedis getRedis(){return redisAutoConfigCacheFactory.getCache("redis.game");}

    @Override
    public boolean addTimes(String key, int timesLimit, int timeLimit) {
        Object obj = getRedis().getString(key);
        if (obj == null) {
            getRedis().set(key, timeLimit, 1);
            return true;
        }
        else {
            Integer times = (Integer) obj + 1;
            getRedis().set(key, timeLimit, times);
            if (times > timesLimit) {
                if (logger.isDebugEnabled()) {
                    logger.debug("frequency over times " + key + " times:" + times + " timeLimit:" + timeLimit);
                }
                return false;
            }
            else {
                if (logger.isDebugEnabled()) {
                    logger.debug("frequency not over times " + key + " times:" + times + " timeLimit:" + timeLimit);
                }
            }
            return true;
        }
    }

    @Override
    public void addTimes(String key, int timeLimit) {
        Object obj = getRedis().get(key);
        if (obj == null) {
            getRedis().set(key,timeLimit,1);
        }
        else {
            Integer times = (Integer) obj + 1;
            getRedis().set(key, timeLimit, times);
        }
    }

    @Override
    public boolean isTimesOverFlow(String key, int limit) {
        Object obj = getRedis().get(key);
        if (obj == null) {
            return false;
        }
        else {
            Integer times = (Integer) obj;
            if (times > limit) {
                if (logger.isDebugEnabled()) {
                    logger.debug("frequency over times " + key + " times:" + times);
                }
                return true;
            }
            else {
                if (logger.isDebugEnabled()) {
                    logger.debug("frequency not over times " + key + " times:" + times);
                }
            }
            return false;
        }
    }

    @Override
    public void clearTimeCache(String key) {
        getRedis().delete(key);
    }
}
