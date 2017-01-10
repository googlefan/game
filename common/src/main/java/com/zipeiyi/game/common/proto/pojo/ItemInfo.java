package com.zipeiyi.game.common.proto.pojo;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class ItemInfo {

	@Protobuf(fieldType = FieldType.INT32,order = 1,required = true)
	int itemID;
	@Protobuf(fieldType = FieldType.INT32,order = 2,required = true)
	int itemAmount;
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public int getItemAmount() {
		return itemAmount;
	}
	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}
	
	
}
