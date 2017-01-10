package com.zipeiyi.game.common.proto.pojo;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class CardInfo {
	@Protobuf(fieldType = FieldType.STRING,required = true,order = 1)
	private String cardID;
	@Protobuf(fieldType = FieldType.FLOAT, required = true,order = 2)
	private Float netValue;
	@Protobuf(fieldType = FieldType.FLOAT, required = true,order = 3)
	private List<Float> oldNetValues;
	@Protobuf(fieldType = FieldType.OBJECT, order = 4)
	private List<IndustryInfo> industryList;
	
	
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public Float getNetValue() {
		return netValue;
	}
	public void setNetValue(Float netValue) {
		this.netValue = netValue;
	}
	public List<Float> getOldNetValues() {
		return oldNetValues;
	}
	public void setOldNetValues(List<Float> oldNetValues) {
		this.oldNetValues = oldNetValues;
	}
	public List<IndustryInfo> getIndustryList() {
		return industryList;
	}
	public void setIndustryList(List<IndustryInfo> industryList) {
		this.industryList = industryList;
	}  
	
	
}
