package com.zipeiyi.game.login.api.model;

import com.zipeiyi.core.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
public class RegisterRequest {

    private static final Logger logger = LoggerFactory.getLogger(RegisterRequest.class);

    public static RegisterRequest parse(String result) throws IOException {
        try{
            return JSONUtil.fromJSONString(result, RegisterRequest.class);
        }catch(IOException e){
            logger.error("parse json faild, raw json:\n{}", result, e);
            throw e;
        }
    }

    public String phone;
    public String loginPwd;
    public String securityCode;


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
