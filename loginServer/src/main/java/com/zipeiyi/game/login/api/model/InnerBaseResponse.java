package com.zipeiyi.game.login.api.model;

import com.zipeiyi.core.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/26.
 */
public class InnerBaseResponse {
    protected static final Logger logger = LoggerFactory.getLogger(InnerBaseResponse.class);

    public int code;
    public String msg;

    @Override
    public String toString() {
        try {
            return JSONUtil.toJSONString(this, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
