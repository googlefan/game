package com.zipeiyi.game.common.message;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Common {

    @Protobuf(fieldType = FieldType.INT32, required = true, order = 1)
    int cmd;//指令码

    @Protobuf(fieldType = FieldType.INT64, order = 2)
    long time;

    @Protobuf(fieldType = FieldType.INT64, order = 3)
    long uid;//用户id

    @Protobuf(fieldType = FieldType.INT32, order = 4)
    int moduleId; //模块

    @Protobuf(fieldType = FieldType.STRING, order = 5)
    String token; //每次登陆生成唯一的

    @Protobuf(fieldType = FieldType.INT64, order = 6)
    long seque;  //通信序列号

    @Protobuf(fieldType = FieldType.INT32, order = 7)
    int channelCode; // 业务使用

    @Protobuf(fieldType = FieldType.BYTES, order = 8)
    byte[] obj; //具体数据对象



    
    
    @Protobuf(fieldType = FieldType.INT32, order = 9)
    int code;

    @Protobuf(fieldType = FieldType.STRING, order =10)
    String msg; //响应具体信息

    @Protobuf(fieldType = FieldType.INT64, order = 11)
    List<Long> uidList; //推送消息，多用户同时推送
    
    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

//    public Object getObj() {
//        return obj;
//    }
//
//    public void setObj(Object obj) {
//        this.obj = obj;
//    }
    
    

    public long getUid() {
        return uid;
    }

    public byte[] getObj() {
		return obj;
	}

	public void setObj(byte[] obj) {
		this.obj = obj;
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

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
    
    
}
