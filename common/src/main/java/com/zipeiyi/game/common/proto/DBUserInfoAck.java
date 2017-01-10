package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.GameUserInfo;
import com.zipeiyi.game.common.util.ErrorCode;

public class DBUserInfoAck {

	@Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
	int ackResult = ErrorCode.AckResult.SUCCESS.getValue();

	@Protobuf(fieldType = FieldType.OBJECT,order = 2)
	private GameUserInfo userInfo;

	public GameUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(GameUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public int getAckResult() {
		return ackResult;
	}

	public void setAckResult(int ackResult) {
		this.ackResult = ackResult;
	}

//	@Protobuf(fieldType = FieldType.STRING)
//	private String icon;
//	@Protobuf(fieldType = FieldType.INT32)
//	private int sex;
//	@Protobuf(fieldType = FieldType.INT32)
//	private int chip;


	
//	public int getUid() {
//		return uid;
//	}
//	public void setUid(int uid) {
//		this.uid = uid;
//	}
//	public String getUserName() {
//		return userName;
//	}
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//	public String getIcon() {
//		return icon;
//	}
//	public void setIcon(String icon) {
//		this.icon = icon;
//	}
//	public int getSex() {
//		return sex;
//	}
//	public void setSex(int sex) {
//		this.sex = sex;
//	}
////	public String getServerID() {
////		return serverID;
////	}
////	public void setServerID(String serverID) {
////		this.serverID = serverID;
////	}
//	public int getChip() {
//		return chip;
//	}
//	public void setChip(int chip) {
//		this.chip = chip;
//	}
	
	
}
