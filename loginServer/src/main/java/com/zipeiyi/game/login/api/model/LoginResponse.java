package com.zipeiyi.game.login.api.model;

import com.zipeiyi.core.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
public class LoginResponse extends BaseResponse{

    public static LoginResponse parse(String result) throws IOException {
        try{
            return JSONUtil.fromJSONString(result, LoginResponse.class);
        }catch(IOException e){
            logger.error("parse json faild, raw json:\n{}", result, e);
            throw e;
        }
    }

    public Data data;

    public static class Data{
        public String connectPhone;
        public String isLocked;
        public long registTime;
        public long updateTime;
        public String connectEmail;
    }
}
