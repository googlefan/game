package com.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.nio.processor.Processor;
import com.common.nio.socket.Session;
import com.game.constant.LogicEventType;
import com.game.db.redis.RedisDao;
import com.game.db.redis.impl.RedisDaoImp;
import com.game.event.EventEngine;
import com.game.event.GameLogicEvent;
import com.game.listener.TableJobEventListener;
import com.game.manager.ServerMgr;
import com.game.module.TexasHoldemServerImp;
import com.game.vo.UserVo;
import com.zipeiyi.game.common.ModuleCmd;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.GameJoinAck;
import com.zipeiyi.game.common.proto.GameJoinReq;

public class GameAction extends Processor {

	private static final Logger logger = LoggerFactory.getLogger(GameAction.class);
	RedisDao reidsDao = RedisDaoImp.getInstance();
	ServerMgr serverMgr = ServerMgr.getInstance();

	@Override
	public void process(Session session, MessageReq request, MessageRes response) throws Exception {
		// TODO Auto-generated method stub
		// int userID = session.getUserID();
		int cmd = request.getCmd();
		switch (cmd) {

		case ModuleCmd.joinGame:
			enterRoom(session, request, response);
			break;
		case ModuleCmd.chooseCard:
			chooseCard(session, request, response);
			break;
		case ModuleCmd.grabBanker:
			grabBanker(session, request, response);
			break;
		case ModuleCmd.raise:
			raise(session, request, response);
			break;
		case ModuleCmd.escape:
			escape(session, request, response);
			break;
		case ModuleCmd.restartConfirm:
			confirmStartGame(session, request, response);
			break;
		}

	}

	/**
	 * 加入队列
	 */
	private void enterRoom(Session session, MessageReq request, MessageRes response) {
		GameJoinReq req = (GameJoinReq) request.getObj();
		long uid = req.getUid();
		UserVo uvo = serverMgr.getUserById(uid);
		// 为空 存redis
		// 找空房间
		// 创建房间
		if (uvo == null) {
			logger.info(String.format("user not in server uid:[%d]", new Object[] { uid }));
			uvo = serverMgr.addUser(session.getClientId(), uid);
		}

		uvo.setServerID(session.getClientId());
		reidsDao.saveUserVo(uvo);
		if (!uvo.isActive()) {
			logger.info(String.format("user not active uid:[%d]", new Object[] { uid }));
			session.write(response);
			return;
		}
		GameJoinAck ack = new GameJoinAck();
		response.setObj(ack);
		session.write(response);
		TexasHoldemServerImp.getInstance().enterTable(uvo);
	}

	// 选择卡牌
	private void chooseCard(Session session, MessageReq request, MessageRes response) {
		GameLogicEvent event = new GameLogicEvent();
		event.setEventType(LogicEventType.CHOOSE_CARDS);
		EventEngine.getInstance().event(event);
	}

	// 抢庄
	private void grabBanker(Session session, MessageReq request, MessageRes response) {

	}

	// 下注
	private void raise(Session session, MessageReq request, MessageRes response) {

	}

	// 逃跑
	private void escape(Session session, MessageReq request, MessageRes response) {

	}

	// 重新确认进入游戏
	private void confirmStartGame(Session session, MessageReq request, MessageRes response) {

	}
}