package com.zipeiyi.game.common.proto;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.BalanceDBInfo;

public class DBGameBalanceReq {

	@Protobuf(fieldType = FieldType.OBJECT)
	private List<BalanceDBInfo> balanceList;

	public List<BalanceDBInfo> getBalanceList() {
		return balanceList;
	}

	public void setBalanceList(List<BalanceDBInfo> balanceList) {
		this.balanceList = balanceList;
	}
	
}
