package com.zipeiyi.game.login.exception;

/**
 * 用户被封禁
 * 
 * 
 * @author zhangxiaoqiang
 */
public class UserBlockedException extends Exception {

    private static final long serialVersionUID = -1985442335757480592L;

    private int errorType;

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

}
