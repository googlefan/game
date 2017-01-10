package com.zipeiyi.game.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhuhui on 16-12-12.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 4970091971561275695L;
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
    private int passwordChange;
    private long siteConnected;
    private boolean isDirect;
    private int loginNum;

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

    public int getPasswordChange() {
        return passwordChange;
    }

    public void setPasswordChange(int passwordChange) {
        this.passwordChange = passwordChange;
    }

    public long getSiteConnected() {
        return siteConnected;
    }

    public void setSiteConnected(long siteConnected) {
        this.siteConnected = siteConnected;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public void setDirect(boolean direct) {
        isDirect = direct;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

}
