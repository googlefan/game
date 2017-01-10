package com.zipeiyi.game.common.proto.push;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.google.common.collect.Lists;

/**
 * 游戏组中退出
 * @author Administrator
 *
 */
public class GameExitTablePush {

	@Protobuf(fieldType = FieldType.STRING)
	private String tableID;
	@Protobuf(fieldType = FieldType.INT64)
	private List<Long> users = Lists.newArrayList();
	
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	public List<Long> getUsers() {
		return users;
	}
	public void setUsers(List<Long> users) {
		this.users = users;
	}

	
}
