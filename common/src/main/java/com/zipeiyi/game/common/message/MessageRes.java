package com.zipeiyi.game.common.message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.util.ErrorCode;

import java.util.List;

/**
 * Created by zhangxiaoqiang on 16/12/6.
 */
public class MessageRes extends Message {

    @Protobuf(fieldType = FieldType.INT32, order = 6)
    int code= ErrorCode.CommonResult.SUCCESS.getCode();

    @Protobuf(fieldType = FieldType.STRING, order = 7)
    String msg; //响应具体信息

    @Protobuf(fieldType = FieldType.INT64, order = 8)
    List<Long> uidList; //推送消息，多用户同时推送

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Long> getUidList() {
        return uidList;
    }

    public void setUidList(List<Long> uidList) {
        this.uidList = uidList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MessageRes{" +
                "cmd=" + cmd +
                ",code=" + code +
                ", msg='" + msg + '\'' +
                ", uidList=" + uidList +
                ", moduleId=" + moduleId +
                ", uid='" + uid + '\'' +
                ", seque='" + seque + '\'' +
                ", obj=" + obj +
                '}';
    }
}
