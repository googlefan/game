package com.zipeiyi.game.common.message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by zhangxiaoqiang on 16/12/6.
 */
public class MessageReq extends Message implements Cloneable{

    long time;

    String token; //每次登陆生成唯一的

    int channelCode; // 业务使用

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(int channelCode) {
        this.channelCode = channelCode;
    }

    @Override
    public MessageReq clone() {
        MessageReq msgReq = null;
        try {
            msgReq = (MessageReq) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return msgReq;
    }

    @Override
    public String toString() {
        return "MessageReq{" +
                "time=" + time +
                ", token='" + token + '\'' +
                ", channelCode=" + channelCode +
                ", moduleId=" + moduleId +
                ", uid='" + uid + '\'' +
                ", cmd=" + cmd +
                ", seque=" + seque +
                ", obj=" + obj +
                '}';
    }
}
