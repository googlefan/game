package com.zipeiyi.game.login.api.model;

import com.zipeiyi.core.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/26.
 */
public class UserDateInitRequest {

    private static final Logger logger = LoggerFactory.getLogger(UserDateInitRequest.class);

    public static UserDateInitRequest parse(String result) throws IOException {
        try{
            return JSONUtil.fromJSONString(result, UserDateInitRequest.class);
        }catch(IOException e){
            logger.error("parse json faild, raw json:\n{}", result, e);
            throw e;
        }
    }

    public long uid;
    public String sign;
    public long timestamp;


    @Override
    public String toString() {
        try{
            return JSONUtil.toJSONString(this,true);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
