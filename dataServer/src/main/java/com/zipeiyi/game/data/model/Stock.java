package com.zipeiyi.game.data.model;

import java.io.Serializable;

/**
 * Created by zhuhui on 16-12-13.
 */
public class Stock implements Serializable {
    private static final long serialVersionUID = 2261966084975177154L;
    // 所属upid
    private String upidId;
    // 所属行业
    private String broadId;
    // 股票名称
    private String secName;
    // 股票代码
    private String secId;

    public String getUpidId() {
        return upidId;
    }

    public void setUpidId(String upidId) {
        this.upidId = upidId;
    }


    public String getBroadId() {
        return broadId;
    }

    public void setBroadId(String broadId) {
        this.broadId = broadId;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

}
