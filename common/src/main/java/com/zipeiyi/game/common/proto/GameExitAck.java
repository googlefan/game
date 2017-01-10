package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.util.ErrorCode;

public class GameExitAck {
	
	@Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
	int ackResult = ErrorCode.AckResult.SUCCESS.getValue();

	@Protobuf(fieldType = FieldType.INT32,order = 2)
	int punishCD;

	public int getPunishCD() {
		return punishCD;
	}

	public void setPunishCD(int punishCD) {
		this.punishCD = punishCD;
	}

	public int getAckResult() {
		return ackResult;
	}

	public void setAckResult(int ackResult) {
		this.ackResult = ackResult;
	}
	
	
	
}
