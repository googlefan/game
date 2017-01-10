package com.game.vo;

import java.util.List;

public class Card {
	
	private String cardID;	
	private String cardName;	
//	private int cardValue;
	private float netValue;
	private List<Float> oldValues;
	private List<Industry> industryList;

	
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
//	public int getCardValue() {
//		return cardValue;
//	}
//	public void setCardValue(int cardValue) {
//		this.cardValue = cardValue;
//	}
	public float getNetValue() {
		return netValue;
	}
	public void setNetValue(float netValue) {
		this.netValue = netValue;
	}
	
	public List<Float> getOldValues() {
		return oldValues;
	}
	public void setOldValues(List<Float> oldValues) {
		this.oldValues = oldValues;
	}
	public List<Industry> getIndustryList() {
		return industryList;
	}
	public void setIndustryList(List<Industry> industryList) {
		this.industryList = industryList;
	}
	
	

}
