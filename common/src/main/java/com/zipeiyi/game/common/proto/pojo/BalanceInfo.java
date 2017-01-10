package com.zipeiyi.game.common.proto.pojo;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class BalanceInfo {
	
	@Protobuf(fieldType = FieldType.INT64, order = 1, required = true)
	private long userID;
	@Protobuf(fieldType = FieldType.INT32, order = 2, required = true)
	private Integer winMoney; // 输是负值

	@Protobuf(fieldType = FieldType.FLOAT, order = 3, required = true)
	private Float darkCardVal;
	

//	@Protobuf(fieldType = FieldType.INT64)
//	private Float openCardVal;
	
	@Protobuf(fieldType = FieldType.INT32)
	private Integer totalChip;  
	
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public Integer getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(Integer winMoney) {
		this.winMoney = winMoney;
	}
	
	
	public Float getDarkCardVal() {
		return darkCardVal;
	}
	public void setDarkCardVal(Float darkCardVal) {
		this.darkCardVal = darkCardVal;
	}
//	public Float getOpenCardVal() {
//		return openCardVal;
//	}
//	public void setOpenCardVal(Float openCardVal) {
//		this.openCardVal = openCardVal;
//	}
	public Integer getTotalChip() {
		return totalChip;
	}
	public void setTotalChip(Integer totalChip) {
		this.totalChip = totalChip;
	}
	
}
