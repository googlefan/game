package com.zipeiyi.game.login.api.model;

import com.zipeiyi.core.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
public class LoginRequest {

    private static final Logger logger = LoggerFactory.getLogger(LoginRequest.class);

    public static LoginRequest parse(String result) throws IOException {
        try{
            return JSONUtil.fromJSONString(result, LoginRequest.class);
        }catch(IOException e){
            logger.error("parse json faild, raw json:\n{}", result, e);
            throw e;
        }
    }

    public String registId;
    public String loginPwd;


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
