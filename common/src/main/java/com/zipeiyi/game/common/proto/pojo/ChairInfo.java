package com.zipeiyi.game.common.proto.pojo;


import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class ChairInfo {
	
	@Protobuf(fieldType = FieldType.INT32,order = 1,required = true)
	private Integer chairSlot;
	@Protobuf(fieldType = FieldType.OBJECT,order = 2,required = true)
	private GameUserInfo userInfo;
	@Protobuf(fieldType = FieldType.OBJECT,order = 3,required = true)
	private CardInfo openCard;
//	@Protobuf(fieldType = FieldType.OBJECT,order = 1,required = true)
//	private long darkCard;
	@Protobuf(fieldType = FieldType.INT32,order = 4)
	private Integer status;
	public Integer getChairSlot() {
		return chairSlot;
	}
	public void setChairSlot(Integer chairSlot) {
		this.chairSlot = chairSlot;
	}
	public GameUserInfo getUserInfo() { 
		return userInfo;
	}
	public void setUserInfo(GameUserInfo userInfo) {
		this.userInfo = userInfo;
	}

//	public CardInfo getOpenCard() {
//		return openCard;
//	}
//	public void setOpenCard(CardInfo openCard) {
//		this.openCard = openCard;
//	}
//	public CardInfo getDarkCard() {
//		return darkCard;
//	}
//	public void setDarkCard(CardInfo darkCard) {
//		this.darkCard = darkCard;
//	}
	
	
	public Integer getStatus() {
		return status;
	}
//	public long getOpenCard() {
//		return openCard;
//	}
//	public void setOpenCard(long openCard) {
//		this.openCard = openCard;
//	}
//	public long getDarkCard() {
//		return darkCard;
//	}
//	public void setDarkCard(long darkCard) {
//		this.darkCard = darkCard;
//	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public CardInfo getOpenCard() {
		return openCard;
	}
	public void setOpenCard(CardInfo openCard) {
		this.openCard = openCard;
	}
	
}
