package com.zipeiyi.game.common.proto.push;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zipeiyi.game.common.proto.pojo.TableInfo;

/**
 * 游戏组产生  通知client
 * @author tpp
 *
 */
public class GameTablePush {

//	@Protobuf(fieldType = FieldType.INT32,required = true )
//	private List<Integer> playerIDs;  // 
	
	@Protobuf(fieldType = FieldType.OBJECT,required = true )
	private TableInfo tableInfo;

	public TableInfo getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}
	
	
}
