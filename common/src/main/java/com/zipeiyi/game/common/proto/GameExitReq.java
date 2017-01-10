package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class GameExitReq {

	@Protobuf(fieldType = FieldType.STRING)
	String tableID;
	@Protobuf(fieldType = FieldType.INT64)
	Long uid;
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	
}
