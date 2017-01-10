package com.zipeiyi.game.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zhuhui on 16-12-13.
 */
public class Upid implements Serializable {
    private String upidId;
    private String hashCode;
    private List<Broad> broadList;
    private Date createDate;

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

    public List<Broad> getBroadList() {
        return broadList;
    }

    public void setBroadList(List<Broad> broadList) {
        this.broadList = broadList;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
