package com.zipeiyi.game.login.service.antispam.impl;

import com.zipeiyi.game.common.util.RedisUtil;
import com.zipeiyi.game.login.service.antispam.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zhangxiaoqiang on 16/12/5.
 */
@Service(value="frequency")
public class FrequencyImpl implements Frequency {

    private static final Logger logger = LoggerFactory.getLogger(FrequencyImpl.class);

    @Override
    public boolean addTimes(String key, int timesLimit, int timeLimit) {
        Object obj = RedisUtil.getRedis().getString(key);
        if (obj == null) {
            RedisUtil.getRedis().set(key, timeLimit, 1);
            return true;
        }
        else {
            Integer times = (Integer) obj + 1;
            RedisUtil.getRedis().set(key, timeLimit, times);
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
        Object obj = RedisUtil.getRedis().get(key);
        if (obj == null) {
            RedisUtil.getRedis().set(key,timeLimit,1);
        }
        else {
            Integer times = (Integer) obj + 1;
            RedisUtil.getRedis().set(key, timeLimit, times);
        }
    }

    @Override
    public boolean isTimesOverFlow(String key, int limit) {
        Object obj = RedisUtil.getRedis().get(key);
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
        RedisUtil.getRedis().delete(key);
    }
}
