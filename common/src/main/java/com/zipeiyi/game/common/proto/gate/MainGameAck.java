package com.zipeiyi.game.common.proto.gate;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.UserInfo;
import com.zipeiyi.game.common.util.ErrorCode;

/**
 * Created by Administrator on 2017/1/4.
 */
public class MainGameAck {

	@Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
	int ackResult = ErrorCode.AckResult.SUCCESS.getValue();

    @Protobuf(fieldType = FieldType.OBJECT,order=2)
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

	public int getAckResult() {
		return ackResult;
	}

	public void setAckResult(int ackResult) {
		this.ackResult = ackResult;
	}
    
    
}
