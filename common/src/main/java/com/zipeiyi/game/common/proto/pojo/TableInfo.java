package com.zipeiyi.game.common.proto.pojo;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class TableInfo {

	@Protobuf(fieldType = FieldType.STRING,order = 1,required = true)
	private String tableID;
	@Protobuf(fieldType = FieldType.OBJECT,order = 2)
	private List<ChairInfo> chairs;
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	public List<ChairInfo> getChairs() {
		return chairs;
	}
	public void setChairs(List<ChairInfo> chairs) {
		this.chairs = chairs;
	}
	
}
