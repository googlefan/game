package com.zipeiyi.game.login.api.model;

import com.zipeiyi.core.common.utils.JSONUtil;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/26.
 */
public class UserDataInitResponse extends InnerBaseResponse  {

    public static UserDataInitResponse parse(String result) throws IOException {
        try{
            return JSONUtil.fromJSONString(result, UserDataInitResponse.class);
        }catch(IOException e){
            logger.error("parse json faild, raw json:\n{}", result, e);
            throw e;
        }
    }
}
