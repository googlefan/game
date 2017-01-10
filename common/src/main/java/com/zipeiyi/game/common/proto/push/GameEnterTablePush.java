package com.zipeiyi.game.common.proto.push;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.ChairInfo;

public class GameEnterTablePush {
	
	@Protobuf(fieldType = FieldType.STRING)
	private String tableID;
	@Protobuf(fieldType = FieldType.OBJECT)
	private List<ChairInfo> enterList;
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	public List<ChairInfo> getEnterList() {
		return enterList;
	}
	public void setEnterList(List<ChairInfo> enterList) {
		this.enterList = enterList;
	}
	
	

}
