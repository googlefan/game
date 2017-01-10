package com.zipeiyi.game.login.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * account: zhangxiaoqiang
 * Date: 13-7-15
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
public class User implements Serializable {

    private static final long serialVersionUID = -8211332961163241530L;

    // -------------- state --------------
    public static final int UNACTIVE = 0; //目前没有激活问题
    public static final int CLOSURE = 10;
    public static final int ACTIVE = 100;
    public static final int BIND_MOBILE = 200;

    // -------------- auth,后台使用游戏相同帐户，分配不同权限 --------------
    public static final int AUTH_COMMON = 0;//普通用户，只有游戏权限
    public static final int AUTH_SELLER = 10;
    public static final int AUTH_EDITER = 20;
    public static final int AUTH_SALESMAN = 30;
    public static final int AUTH_CUSTOM_SERVICE = 40;
    public static final int AUTH_VOUCHER_EDIT = 50;
    public static final int AUTH_CUSTOM_SERVICE1 = 60; // 一线客服
    public static final int AUTH_MERCHANT_MANAGE = 70;// 商户管理后台
    public static final int AUTH_ADMIN = 90;
    public static final int AUTH_SUPPER = 100;

    // -------------- params --------------
    private long id;
    private String account;
    private String name;
    private String token;
    private int auth;
    private int state;
    private Date regTime;
    private Date lastLoginTime;
    private String regIp;
    private String lastLoginIp;
    private int siteConnected;
    private boolean isDirect;
    private int loginNum;
    private int figure;


    public String getRegIp() {
        return regIp;
    }

    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isActive() {
        return state >= ACTIVE;
    }

    public boolean isClosure() {
        return state == CLOSURE;
    }

    public boolean isBindMobile() {
        return state >= BIND_MOBILE;
    }

    public int getSiteConnected() {
        return siteConnected;
    }

    public void setSiteConnected(int siteConnected) {
        this.siteConnected = siteConnected;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public void setDirect(boolean isDirect) {
        this.isDirect = isDirect;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", auth=" + auth +
                ", state=" + state +
                ", regTime=" + regTime +
                ", lastLoginTime=" + lastLoginTime +
                ", regIp='" + regIp + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", siteConnected=" + siteConnected +
                ", isDirect=" + isDirect +
                ", loginNum=" + loginNum +
                ", figure=" + figure +
                '}';
    }
}

