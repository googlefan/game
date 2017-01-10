package com.zipeiyi.game.common.proto.push;

import java.util.Collection;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.BalanceInfo;

public class GameBalancePush {
	
	@Protobuf(fieldType = FieldType.STRING)
	private String tableID;
	
	@Protobuf(fieldType = FieldType.OBJECT)
	private Collection<BalanceInfo> balenceInfoList;

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	public Collection<BalanceInfo> getBalenceInfoList() {
		return balenceInfoList;
	}

	public void setBalenceInfoList(Collection<BalanceInfo> balenceInfoList) {
		this.balenceInfoList = balenceInfoList;
	}

	
	
	
}


	