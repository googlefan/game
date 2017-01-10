package com.zipeiyi.game.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhuhui on 16-12-13.
 */
public class Broad implements Serializable {
    private static final long serialVersionUID = 3610014512983348478L;
    private List<Stock> stockList;
    // 行业名称
    private String broadName;
    // 行业id
    private String broadId;
    // 行业占比
    private String broadProportion;
    // 所属upid
    private String upidId;

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public String getBroadName() {
        return broadName;
    }

    public void setBroadName(String broadName) {
        this.broadName = broadName;
    }

    public String getBroadId() {
        return broadId;
    }

    public void setBroadId(String broadId) {
        this.broadId = broadId;
    }

    public String getBroadProportion() {
        return broadProportion;
    }

    public void setBroadProportion(String broadProportion) {
        this.broadProportion = broadProportion;
    }

    public String getUpidId() {
        return upidId;
    }

    public void setUpidId(String upidId) {
        this.upidId = upidId;
    }
}
