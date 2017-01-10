package com.zipeiyi.game.common.proto.pojo;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class BalanceDBInfo {
	
	@Protobuf(fieldType = FieldType.INT32,order = 1)
	private Integer userID;
	@Protobuf(fieldType = FieldType.INT32,order = 2)
	private Integer winMoney;  //输是负值
	@Protobuf(fieldType = FieldType.BOOL,order = 3)
	private Integer isBanker;
	@Protobuf(fieldType = FieldType.INT32,order = 4)
	private Integer betScore;
	@Protobuf(fieldType = FieldType.OBJECT,order = 5)
	private List<CardInfo> openCardList;
	@Protobuf(fieldType = FieldType.OBJECT,order = 6)
	private List<CardInfo> darkCardList;

	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Integer getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(Integer winMoney) {
		this.winMoney = winMoney;
	}
	public Integer getIsBanker() {
		return isBanker;
	}
	public void setIsBanker(Integer isBanker) {
		this.isBanker = isBanker;
	}
	public Integer getBetScore() {
		return betScore;
	}
	public void setBetScore(Integer betScore) {
		this.betScore = betScore;
	}
	public List<CardInfo> getOpenCardList() {
		return openCardList;
	}
	public void setOpenCardList(List<CardInfo> openCardList) {
		this.openCardList = openCardList;
	}
	public List<CardInfo> getDarkCardList() {
		return darkCardList;
	}
	public void setDarkCardList(List<CardInfo> darkCardList) {
		this.darkCardList = darkCardList;
	}
	
}
