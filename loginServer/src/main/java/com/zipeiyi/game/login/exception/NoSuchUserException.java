package com.zipeiyi.game.login.exception;

/**
 * 用户不存在或者密码错
 * 
 * 
 * @author zhangxiaoqiang
 */
public class NoSuchUserException extends Exception {

    private static final long serialVersionUID = 3207078258305552324L;

    private int errorType;

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

}
