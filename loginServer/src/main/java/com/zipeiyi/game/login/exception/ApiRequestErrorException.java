package com.zipeiyi.game.login.exception;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
public class ApiRequestErrorException extends Exception {

    public ApiRequestErrorException() {

    }

    public ApiRequestErrorException(String message) {
        super(message);
    }

    public ApiRequestErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiRequestErrorException(Throwable cause) {
        super(cause);
    }

    public ApiRequestErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
