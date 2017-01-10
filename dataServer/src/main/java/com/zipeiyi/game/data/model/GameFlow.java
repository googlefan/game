package com.zipeiyi.game.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by zhuhui on 16-12-12.
 */
public class GameFlow implements Serializable {
    private static final long serialVersionUID = -8939929231595812415L;
    private long id;
    private long userId;
    // 庄/散
    private boolean isDealer;
    // 赢取筹码数值
    private Integer winChips;
    // 加倍值
    private Integer doubleValue;
    // 明牌UPID
    private String openUpid;
    // 暗牌UPID
    private String hideUpid;
    // 创建时间
    private Date createTime;

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

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }

    public Integer getWinChips() {
        return winChips;
    }

    public void setWinChips(Integer winChips) {
        this.winChips = winChips;
    }

    public Integer getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Integer doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String getOpenUpid() {
        return openUpid;
    }

    public void setOpenUpid(String openUpid) {
        this.openUpid = openUpid;
    }

    public String getHideUpid() {
        return hideUpid;
    }

    public void setHideUpid(String hideUpid) {
        this.hideUpid = hideUpid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
