package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.util.ErrorCode;

public class GameHandExitAck {

	@Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
	int ackResult = ErrorCode.AckResult.SUCCESS.getValue();

	@Protobuf(fieldType = FieldType.INT32,order = 2)
	private Integer punishCD; //惩罚时间
	@Protobuf(fieldType = FieldType.INT32,order = 3)
	private Integer fine; //罚款
	public Integer getPunishCD() {
		return punishCD;
	}
	public void setPunishCD(Integer punishCD) {
		this.punishCD = punishCD;
	}
	public Integer getFine() {
		return fine;
	}
	public void setFine(Integer fine) {
		this.fine = fine;
	}
	public int getAckResult() {
		return ackResult;
	}
	public void setAckResult(int ackResult) {
		this.ackResult = ackResult;
	}
	
	
	
}
