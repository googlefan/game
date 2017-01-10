package com.game.db;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.nio.socket.ClientSessionMgr;
import com.common.nio.socket.Session;
import com.game.constant.DBParam;
import com.game.db.dao.CenterDBDao;
import com.google.common.collect.Maps;
import com.zipeiyi.game.common.ModuleCmd;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.util.JsonUtil;

public class CenterDBService {

	private static final Logger logger = LoggerFactory.getLogger(CenterDBDao.class);
	private static CenterDBDao dao = CenterDBDao.getInstance();
	private String sessionId = "dbServer";

	private Session session = ClientSessionMgr.getInstance().getSession(sessionId);
	ConcurrentMap<Long, DBParam> reqQueue;
	public static AtomicInteger automicId = new AtomicInteger(1);

	private static CenterDBService instance;

	public static CenterDBService getInstance() {
		if (instance == null) {
			instance = new CenterDBService();
		}

		return instance;
	}

	public CenterDBService() {
		reqQueue = Maps.newConcurrentMap();
		onMessageTask();
	}

	public void sendReqMessage(DBParam param) {
		long sque = automicId.getAndIncrement();
		param.getRequest().setSeque(sque);
		reqQueue.put(sque, param);
		session.send(param.getRequest());
	}

	private void onMessageTask() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					for (Long sque : reqQueue.keySet()) {

						DBParam param = reqQueue.get(sque);
						MessageRes response = param.getResponse();
						if (response != null) {
							int cmd = response.getCmd();
							logger.info("onMessageTask loop cmd:" + cmd + " obj:"
									+ JsonUtil.getJsonFromBean(response.getObj()));
							switch (cmd) {
							case ModuleCmd.center_userByID:
								dao.onGetUserInfoByUid(param.getReqDataMap(), response);
								break;
							case ModuleCmd.center_cardList:
								dao.onGetCardsList(param.getReqDataMap(), response);
								break;
							}
							reqQueue.remove(sque);
						}
					}
				}
			}
		}).start();
	}

	public void onMessage(MessageRes response) {

		if (response == null) {
			logger.error("db response is null!");
		}
		long sque = response.getSeque();

		if (reqQueue.containsKey(sque)) {
			DBParam param = reqQueue.get(sque);
			logger.info("db response:: seque::" + response.getSeque() + " response::" + param.getResponse());
			if (param.getResponse() == null) {
				param.setResponse(response);
				reqQueue.put(sque, param);
			}
		} else {
			logger.warn("onMessage Response Has Received sque::" + sque + "::::" + response);
		}
	}

}
