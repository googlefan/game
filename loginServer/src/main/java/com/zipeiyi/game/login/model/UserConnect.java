package com.zipeiyi.game.login.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangxiaoqiang on 16/12/12.
 */
public class UserConnect implements Serializable {

    // connect site
    public static final int SITE_NOMAL = 0;

    public static final int SITE_QQ = 1;

    public static final int SITE_WEIXIN = 2;

    public static final int SITE_SINA = 3;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long id;

    private int site;//来源类型

    private long siteId; //使用userid

    private String siteEmail;//sina返回的是邮箱，qq使用前缀＋openid模拟

    private Date connectDate;

    private long userId;

    private String name; //用户名

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public Date getConnectDate() {
        return connectDate;
    }

    public void setConnectDate(Date connectDate) {
        this.connectDate = connectDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSiteEmail() {
        return siteEmail;
    }

    public void setSiteEmail(String siteEmail) {
        this.siteEmail = siteEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
