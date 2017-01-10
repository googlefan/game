package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * 
 * @author tpp
 *
 */
public class GameReStartConfirmReq {
	@Protobuf(fieldType = FieldType.STRING,order = 2)
	private String tableID;
	@Protobuf(fieldType = FieldType.INT32,order = 1)
	private Long userID;
	@Protobuf(fieldType = FieldType.BOOL)
	private boolean isReStart;
	
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}


	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public boolean isReStart() {
		return isReStart;
	}
	public void setReStart(boolean isReStart) {
		this.isReStart = isReStart;
	}
	
}
