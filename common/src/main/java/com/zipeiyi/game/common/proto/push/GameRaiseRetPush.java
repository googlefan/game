package com.zipeiyi.game.common.proto.push;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.GameScoreInfo;

/**
 * 下注结果推送
 * @author Administrator
 *
 */
public class GameRaiseRetPush {

	@Protobuf(fieldType = FieldType.STRING)
	private String tableID;
	@Protobuf(fieldType = FieldType.OBJECT)
	List<GameScoreInfo> raiseList;

	

	public List<GameScoreInfo> getRaiseList() {
		return raiseList;
	}

	public void setRaiseList(List<GameScoreInfo> raiseList) {
		this.raiseList = raiseList;
	}

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
}

