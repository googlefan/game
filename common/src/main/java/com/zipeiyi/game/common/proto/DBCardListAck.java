package com.zipeiyi.game.common.proto;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.CardInfo;
import com.zipeiyi.game.common.util.ErrorCode;

public class DBCardListAck {

	@Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
	int ackResult = ErrorCode.AckResult.SUCCESS.getValue();

	@Protobuf(fieldType = FieldType.OBJECT, order = 2)
	private List<CardInfo> cardList;

	public List<CardInfo> getCardList() {
		return cardList;
	}

	public void setCardList(List<CardInfo> cardList) {
		this.cardList = cardList;
	}

	public int getAckResult() {
		return ackResult;
	}

	public void setAckResult(int ackResult) {
		this.ackResult = ackResult;
	}
	
	

}
