package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class GameGrabBankerReq {

	@Protobuf(fieldType = FieldType.INT32)
	private Integer userID;
	@Protobuf(fieldType = FieldType.STRING)
	private String tableID;
	@Protobuf(fieldType = FieldType.INT32)
	private Integer score;
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	
}
