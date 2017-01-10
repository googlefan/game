package com.zipeiyi.game.login.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhuhui on 16-12-12.
 */
public class Card implements Serializable {
    private static final long serialVersionUID = 2157668480989615508L;
    private long id;
    private String upidId;
    private String hashCode;
    private String upidJson;
    private CardTypeEnum type;
    private boolean isUsed;
    // 最新净值
    private BigDecimal lastRateValue;
    // 最近30天的净值(逗号分隔)
    private String rateValues;
    // 创建时间
    private Date createTime;
    private Date updateTime;

    public enum CardTypeEnum {
        // 赠送卡牌
        TYPE_1,
        // 每局随机抽取暗牌
        TYPE_2;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUpidId() {
        return upidId;
    }

    public void setUpidId(String upidId) {
        this.upidId = upidId;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getUpidJson() {
        return upidJson;
    }

    public void setUpidJson(String upidJson) {
        this.upidJson = upidJson;
    }

    public CardTypeEnum getType() {
        return type;
    }

    public void setType(CardTypeEnum type) {
        this.type = type;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public BigDecimal getLastRateValue() {
        return lastRateValue;
    }

    public void setLastRateValue(BigDecimal lastRateValue) {
        this.lastRateValue = lastRateValue;
    }

    public String getRateValues() {
        return rateValues;
    }

    public void setRateValues(String rateValues) {
        this.rateValues = rateValues;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
