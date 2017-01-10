package com.zipeiyi.game.login.api.model;

import com.zipeiyi.core.common.utils.JSONUtil;

import java.io.IOException;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
public class ForgetPassResponse extends BaseResponse{

    public static ForgetPassResponse parse(String result) throws IOException {
        try{
            return JSONUtil.fromJSONString(result, ForgetPassResponse.class);
        }catch(IOException e){
            logger.error("parse json faild, raw json:\n{}", result, e);
            throw e;
        }
    }
}
