package com.zipeiyi.game.common.message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Message {


    int moduleId; //模块

    int cmd;//指令码

    long uid;//用户id

    Object obj; //具体数据对象

    long seque;  //通信序列号

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
    
    

    public long getUid() {
        return uid;
    }



	public void setUid(long uid) {
        this.uid = uid;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public long getSeque() {
        return seque;
    }

    public void setSeque(long seque) {
        this.seque = seque;
    }
}
