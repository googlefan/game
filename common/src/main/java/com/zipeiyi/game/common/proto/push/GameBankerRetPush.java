package com.zipeiyi.game.common.proto.push;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.GameScoreInfo;

/**
 * 庄家结果
 * @author Administrator
 *
 */
public class GameBankerRetPush {

	@Protobuf(fieldType = FieldType.INT32)
	private long bakerID;  //庄家id
	@Protobuf(fieldType = FieldType.STRING)
	private String tableID;
	@Protobuf(fieldType = FieldType.OBJECT)
	private List<GameScoreInfo> scoreList;

	
	
	public long getBakerID() {
		return bakerID;
	}
	public void setBakerID(long bakerID) {
		this.bakerID = bakerID;
	}
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	public List<GameScoreInfo> getScoreList() {
		return scoreList;
	}
	public void setScoreList(List<GameScoreInfo> scoreList) {
		this.scoreList = scoreList;
	}
	
	
	
}
