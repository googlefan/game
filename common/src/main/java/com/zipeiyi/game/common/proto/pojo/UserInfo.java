package com.zipeiyi.game.common.proto.pojo;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * 大厅数据获取user数据
 *
 * @author Administrator
 */
public class UserInfo extends GameUserInfo {

    @Protobuf(fieldType = FieldType.INT32, required = true,order = 8)
    int diamond;

    @Protobuf(fieldType = FieldType.BOOL,order = 9)
    boolean isNew;

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
