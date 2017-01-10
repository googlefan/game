package com.game.vo;

import java.util.List;


/**
 * 
 * @author Administrator
 *
 */
public class Industry {

	private String industryName; //行业名称
	private Float industryPercent; //行业占比
	private List<Stock> stockList;
	
	
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
	public List<Stock> getStockList() {
		return stockList;
	}
	public void setStockList(List<Stock> stockList) {
		this.stockList = stockList;
	}
	
}
