package com.zipeiyi.game.common.proto.push;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class GateUserOffLinePush {

	@Protobuf(fieldType = FieldType.INT32)
	int uid;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	
}
