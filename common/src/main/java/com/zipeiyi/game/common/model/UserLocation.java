package com.zipeiyi.game.common.model;

import java.io.Serializable;

/**
 * Created by zhangxiaoqiang on 16/12/6.
 */
public class UserLocation implements Serializable {

    private String hostName;
    private String uid;
    private int roomId;
    private int index;
    private int status;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "hostName='" + hostName + '\'' +
                ", uid='" + uid + '\'' +
                ", roomId=" + roomId +
                ", index=" + index +
                ", status=" + status +
                '}';
    }
}
