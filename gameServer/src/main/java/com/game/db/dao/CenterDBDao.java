package com.game.db.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.constant.DBParam;
import com.game.db.CenterDBService;
import com.game.manager.ServerMgr;
import com.zipeiyi.game.common.ModuleCmd;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBCardListAck;
import com.zipeiyi.game.common.proto.DBCardListReq;
import com.zipeiyi.game.common.proto.DBUserInfoAck;
import com.zipeiyi.game.common.proto.DBUserInfoReq;
import com.zipeiyi.game.common.util.JsonUtil;

/**
 * 从DB中心服获取数据
 * 
 * @author tpp
 *
 */
public class CenterDBDao {

	private static final Logger logger = LoggerFactory.getLogger(CenterDBDao.class);

//	private CenterDBService dbService = CenterDBService.getInstance();

	private static CenterDBDao instance;

	public static CenterDBDao getInstance() {
		if (instance == null) {
			instance = new CenterDBDao();
		}
		return instance;
	}

	/**
	 * 
	 * @param userID
	 * @return
	 */
	public void getUserInfoByUid(long uid) {

		MessageReq req = new MessageReq();
		req.setCmd(ModuleCmd.center_userByID);
		DBUserInfoReq obj = new DBUserInfoReq();
		obj.setUid(uid);
		req.setObj(obj);

		DBParam param = new DBParam();
		param.setRequest(req);
		param.getReqDataMap().put("uid", uid); 
		CenterDBService.getInstance().sendReqMessage(param);
	}

	public void getCardsList(String tableID, int cardAmount) {
		MessageReq req = new MessageReq();
		req.setCmd(ModuleCmd.center_cardList);
		DBCardListReq obj = new DBCardListReq();
		obj.setCardAmount(cardAmount);
		req.setObj(obj);

		DBParam param = new DBParam();
		param.setRequest(req);
		param.getReqDataMap().put("tableID", tableID);
		CenterDBService.getInstance().sendReqMessage(param);
	}

	public void onGetUserInfoByUid(Map<String, Object> data, MessageRes response) {
		logger.info("onGetUserInfoByUid === ");
		DBUserInfoAck recMsg = (DBUserInfoAck) response.getObj();
		long uid = Long.valueOf(String.valueOf(data.get("uid")));
		ServerMgr.getInstance().initUserVo(uid, recMsg);
	}

	public void onGetCardsList(Map<String, Object> data, MessageRes response) {
		logger.info("onGetCardsList === ");
		DBCardListAck recMsg = (DBCardListAck) response.getObj();
		String tableID = String.valueOf(data.get("tableID"));
		ServerMgr.getInstance().initTableCardList(tableID, recMsg);
	}

}
