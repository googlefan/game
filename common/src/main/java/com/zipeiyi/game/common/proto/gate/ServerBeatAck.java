package com.zipeiyi.game.common.proto.gate;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.util.ErrorCode;

/**
 * Created by Administrator on 2017/1/4.
 */
public class ServerBeatAck {
	@Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
	int ackResult = ErrorCode.AckResult.SUCCESS.getValue();

	public int getAckResult() {
		return ackResult;
	}

	public void setAckResult(int ackResult) {
		this.ackResult = ackResult;
	}

	
}
