package com.zipeiyi.game.common.model;

import java.io.Serializable;

/**
 * Created by zhangxiaoqiang on 16/12/13.
 */
public class UserGateLocation implements Serializable{

    private String hostName;
    private long uid;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UserGateLocation{" +
                "hostName='" + hostName + '\'' +
                ", uid=" + uid +
                '}';
    }
}
