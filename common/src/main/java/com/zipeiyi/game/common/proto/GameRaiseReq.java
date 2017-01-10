package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class GameRaiseReq {

	@Protobuf(fieldType = FieldType.INT64)
	private Long userID;
	@Protobuf(fieldType = FieldType.STRING)
	private String tableID;
	@Protobuf(fieldType = FieldType.INT32)
	private Integer betScore;

	
	public Integer getBetScore() {
		return betScore;
	}
	public void setBetScore(Integer betScore) {
		this.betScore = betScore;
	}
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	
	
}
