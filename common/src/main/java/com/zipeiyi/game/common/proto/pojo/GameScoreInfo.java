package com.zipeiyi.game.common.proto.pojo;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class GameScoreInfo {

	@Protobuf(fieldType = FieldType.INT64,order = 1,required = true)
	private long userID;
	@Protobuf(fieldType = FieldType.INT32,order = 2,required = true)
	private Integer score;
	
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
	
}
