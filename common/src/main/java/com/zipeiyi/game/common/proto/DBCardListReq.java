package com.zipeiyi.game.common.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class DBCardListReq {

	@Protobuf(fieldType = FieldType.INT32,order = 1)
	private int cardAmount;

	public int getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(int cardAmount) {
		this.cardAmount = cardAmount;
	}
	
	
}
