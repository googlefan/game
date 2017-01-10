package com.zipeiyi.game.login.api.model;

import com.zipeiyi.core.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
public abstract class BaseResponse {

    protected static final Logger logger = LoggerFactory.getLogger(BaseResponse.class);

    public String status;
    public String errorInfo;
    public String code;
    public String sign;
    public String token;
    public String version;

    @Override
    public String toString() {
        try {
            return JSONUtil.toJSONString(this, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
