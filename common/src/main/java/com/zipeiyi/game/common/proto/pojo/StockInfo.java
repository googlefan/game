package com.zipeiyi.game.common.proto.pojo;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class StockInfo {

	@Protobuf(fieldType = FieldType.STRING,required = true,order = 1)
	private String stockCode;
	@Protobuf(fieldType = FieldType.STRING,required = true,order = 2)
	private String stockName;
	@Protobuf(fieldType = FieldType.FLOAT,required = true,order = 3)
	private Float nowValue;
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public Float getNowValue() {
		return nowValue;
	}
	public void setNowValue(Float nowValue) {
		this.nowValue = nowValue;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	
	
}
