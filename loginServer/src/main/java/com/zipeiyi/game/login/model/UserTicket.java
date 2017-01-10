package com.zipeiyi.game.login.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户登录ticket
 *
 * @author zhangxiaoqiang
 */
public class UserTicket implements Serializable {

    private long id;

    private long userId;

    private Date loginTime; // 登录时间

    private String ticket; // 票～

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
