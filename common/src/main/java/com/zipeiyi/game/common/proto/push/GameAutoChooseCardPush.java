package com.zipeiyi.game.common.proto.push;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class GameAutoChooseCardPush {

	@Protobuf(fieldType = FieldType.STRING)
	private String openCardID;
	
	@Protobuf(fieldType = FieldType.STRING)
	private String darkCardID;

	public String getOpenCardID() {
		return openCardID;
	}

	public void setOpenCardID(String openCardID) {
		this.openCardID = openCardID;
	}

	public String getDarkCardID() {
		return darkCardID;
	}

	public void setDarkCardID(String darkCardID) {
		this.darkCardID = darkCardID;
	}

	
}
