package com.common.nio.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.nio.socket.Session;
import com.game.db.CenterDBService;
import com.zipeiyi.game.common.message.MessageRes;

public class DBClientDispatcher extends AbsDisPatcher {

	private static final Logger logger = LoggerFactory.getLogger(DBClientDispatcher.class);

	private static volatile DBClientDispatcher instance;

	public static DBClientDispatcher getInstance() {

		if (instance == null) {
			synchronized (DBClientDispatcher.class) {
				instance = new DBClientDispatcher();
				return instance;
			}
		}
		return instance;
	}

	public void onDispatcher(Session session, MessageRes response) throws Exception {
		// TODO Auto-generated method stub
		if (response == null) {
			return;
		}

		int module = response.getModuleId();
		int cmd = response.getCmd();

		logger.info(String.format("Receive DBServer Response Message: module:[%d],cmd:[%d],seque:[%d]",
				new Object[] {new Integer(module), new Integer(cmd),new Long(response.getSeque())}));
		CenterDBService.getInstance().onMessage(response);
		
	}

}
