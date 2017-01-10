package com.zipeiyi.game.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zhuhui on 16-12-12.
 */
public class UserProfile implements Serializable {
    private static final long serialVersionUID = 4112412446063803138L;
    private long id;
    private long userId;
    // 胜次
    private Integer winCount;
    // 负次
    private Integer lossCount;
    // 逃跑次数
    private Integer escapeCount;
    // 总资产
    private BigDecimal assets;

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

    public Integer getWinCount() {
        return winCount;
    }

    public void setWinCount(Integer winCount) {
        this.winCount = winCount;
    }

    public Integer getLossCount() {
        return lossCount;
    }

    public void setLossCount(Integer lossCount) {
        this.lossCount = lossCount;
    }

    public Integer getEscapeCount() {
        return escapeCount;
    }

    public void setEscapeCount(Integer escapeCount) {
        this.escapeCount = escapeCount;
    }

    public BigDecimal getAssets() {
        return assets;
    }

    public void setAssets(BigDecimal assets) {
        this.assets = assets;
    }
}
