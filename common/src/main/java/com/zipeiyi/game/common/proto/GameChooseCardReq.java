package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class GameChooseCardReq {

	@Protobuf(fieldType = FieldType.INT32)
	private Integer userID;
	@Protobuf(fieldType = FieldType.STRING)
	private String tableID;
	@Protobuf(fieldType = FieldType.STRING)
	private String openCardID;

	@Protobuf(fieldType = FieldType.STRING)
	private String darkCardID;



	public String getOpenCardID() {
		return openCardID;
	}

	public void setOpenCardID(String openCardID) {
		this.openCardID = openCardID;
	}

	public String getDarkCardID() {
		return darkCardID;
	}

	public void setDarkCardID(String darkCardID) {
		this.darkCardID = darkCardID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	
	
	
}
