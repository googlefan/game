package com.zipeiyi.game.common.proto.pojo;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * 
 * @author Administrator
 *
 */
public class IndustryInfo {

	@Protobuf(fieldType = FieldType.STRING,required = true,order = 1)
	private String industryName; //行业名称
	@Protobuf(fieldType = FieldType.FLOAT,required = true,order = 2)
	private Float industryPercent; //行业占比
	@Protobuf(fieldType = FieldType.OBJECT,required = true,order = 3)
	private List<StockInfo> stockList;
	
	
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public Float getIndustryPercent() {
		return industryPercent;
	}
	public void setIndustryPercent(Float industryPercent) {
		this.industryPercent = industryPercent;
	}
	public List<StockInfo> getStockList() {
		return stockList;
	}
	public void setStockList(List<StockInfo> stockList) {
		this.stockList = stockList;
	}
	
}
