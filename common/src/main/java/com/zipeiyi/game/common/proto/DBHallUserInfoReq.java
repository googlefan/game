package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * 大厅获取用户数据
 *
 * @author Administrator
 */
public class DBHallUserInfoReq {

    @Protobuf(fieldType = FieldType.INT64,order = 1)
    private long uid;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }


}
