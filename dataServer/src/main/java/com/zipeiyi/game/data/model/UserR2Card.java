package com.zipeiyi.game.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhuhui on 16-12-13.
 */
public class UserR2Card implements Serializable {
    private static final long serialVersionUID = -2420025204306199308L;
    private long userId;
    private long cardId;
    private Date createTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
