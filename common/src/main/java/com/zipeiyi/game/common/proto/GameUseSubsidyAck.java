package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.util.ErrorCode;

/**
 * 使用破产补助
 * @author tpp
 *
 */
public class GameUseSubsidyAck {
	
	@Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
	int ackResult = ErrorCode.AckResult.SUCCESS.getValue();

	public int getAckResult() {
		return ackResult;
	}

	public void setAckResult(int ackResult) {
		this.ackResult = ackResult;
	}

	
	
	
}
