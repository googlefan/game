package com.game.module;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.nio.socket.Session;
import com.common.nio.socket.SeverSessionMgr;
import com.common.util.TimeTool;
import com.common.util.Utils;
import com.game.constant.Define;
import com.game.db.dao.CenterDBDao;
import com.game.db.redis.RedisDao;
import com.game.db.redis.impl.RedisDaoImp;
import com.game.event.GameLogicEvent;
import com.game.vo.Card;
import com.game.vo.ChairVo;
import com.game.vo.Industry;
import com.game.vo.Stock;
import com.game.vo.TableVo;
import com.game.vo.UserVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zipeiyi.game.common.ModuleCmd;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.GameChooseCardReq;
import com.zipeiyi.game.common.proto.GameExitAck;
import com.zipeiyi.game.common.proto.GameExitReq;
import com.zipeiyi.game.common.proto.GameGrabBankerAck;
import com.zipeiyi.game.common.proto.GameGrabBankerReq;
import com.zipeiyi.game.common.proto.GameRaiseAck;
import com.zipeiyi.game.common.proto.GameRaiseReq;
import com.zipeiyi.game.common.proto.GameReStartConfirmAck;
import com.zipeiyi.game.common.proto.GameReStartConfirmReq;
import com.zipeiyi.game.common.proto.pojo.BalanceInfo;
import com.zipeiyi.game.common.proto.pojo.CardInfo;
import com.zipeiyi.game.common.proto.pojo.ChairInfo;
import com.zipeiyi.game.common.proto.pojo.GameScoreInfo;
import com.zipeiyi.game.common.proto.pojo.GameUserInfo;
import com.zipeiyi.game.common.proto.pojo.IndustryInfo;
import com.zipeiyi.game.common.proto.pojo.StockInfo;
import com.zipeiyi.game.common.proto.pojo.TableInfo;
import com.zipeiyi.game.common.proto.push.GameAutoChooseCardPush;
import com.zipeiyi.game.common.proto.push.GameBalancePush;
import com.zipeiyi.game.common.proto.push.GameBankerRetPush;
import com.zipeiyi.game.common.proto.push.GameCardsPush;
import com.zipeiyi.game.common.proto.push.GameExitTablePush;
import com.zipeiyi.game.common.proto.push.GameRaiseRetPush;
import com.zipeiyi.game.common.proto.push.GameTablePush;
import com.zipeiyi.game.common.util.JsonUtil;
import com.zipeiyi.game.common.util.StringUtil;

public class TexasHoldemServerImp implements TexasHoldemServer {

	private static final Logger logger = LoggerFactory.getLogger(TexasHoldemServerImp.class);
	private RedisDao redisDao = RedisDaoImp.getInstance();
	private SeverSessionMgr sessionMgr = SeverSessionMgr.getInstance();

	private static TexasHoldemServerImp instance;

	public static TexasHoldemServerImp getInstance() {
		if (instance == null) {
			instance = new TexasHoldemServerImp();
		}
		return instance;
	}

	// 到时操作
	public void runTableJob(Map<String, Object> data) {
		String tableID = data.get("tableID").toString();
		long now = TimeTool.snow();
		logger.info("定时任务开始------------------------"+now);

		TableVo table = redisDao.getTableVo(tableID);
		if (table == null) {
			logger.warn("runTableJob table dosn't exsit!!" + tableID);
			TableVo.cancelTableJob(tableID);
			return;
		}

		synchronized (table) {
			// 判断人数
			List<ChairVo> usedChairs = table.getUsedChairs();
			if (usedChairs.size() < 3) {
				logger.warn("runTableJob table used chair count small more than 3!! tableID:" + tableID+" 人数: "+usedChairs.size() );
				return;
			}
		}
		long lastDoTime = table.getLastdoTime();
		if (!table.isGaming) {
			if (TimeTool.snow() - lastDoTime >= 10 && table.isActive) {
				beginCard(tableID);
				return;
			}
		}

		if (table.isGaming) {
			// 选牌
			if (TimeTool.snow() - lastDoTime >= 30) {
				autoChooseCard(tableID);
				return;
			}

			int bankerSlot = table.getBankerSlot();
			// 抢庄
			if (TimeTool.snow() - lastDoTime >= 5 && bankerSlot < 0) {

				this.autoGrabBanker(tableID);
				return;
			}
			// 下注
			if (TimeTool.snow() - lastDoTime >= 5 && table.getStatus() != Define.GameStatusEnum.BALANCE.getValue()) {
				this.autoRaise(tableID);
				return;
			}

			// 结算阶段完成 进入空闲
			if (TimeTool.snow() - lastDoTime >= 10) {
				this.resetGameData(table.getTableID());
				return;
			}

		}

		// 重新开始游戏
		if (TimeTool.snow() - lastDoTime >= 5 && table.getStatus() == Define.GameStatusEnum.FREE.getValue()) {
			this.autoConfirmStartGame(tableID);
			return;
		}

		System.out.println("任务线程执行耗时===================" + (TimeTool.snow() - now));
	}

	// 开始发牌 请求db服务器
	private void beginCard(String tableID) {
		TableVo table = redisDao.getTableVo(tableID);
		CenterDBDao.getInstance().getCardsList(tableID, table.getUsedChairs().size()*3);
		table.isActive = false;
		redisDao.saveTableVo(table);
	}

	// client推送选牌 回调触发
	public void cardPush(String tableID) {

		TableVo table = redisDao.getTableVo(tableID);
		logger.info("cardPush 推送游戏暗牌。。。。tableID::" + tableID);

		table.isGaming = true; // 标记开始

		redisDao.saveTableVo(table);
		List<ChairVo> usedList = table.getUsedChairs();
		MessageRes response = new MessageRes();
		response.setSeque(-1);
		response.setCmd(ModuleCmd.cardListPush);

		for (ChairVo chair : usedList) {
			GameCardsPush cardPush = new GameCardsPush();
			List<CardInfo> cardList = Lists.newArrayList();

			cardPush.setTableID(tableID);
			cardPush.setCardList(cardList);

			List<Card> list = chair.getCardPool();
			logger.info("推送卡牌内容============="+JsonUtil.getJsonFromBean(list));
			for (Card card : list) {
				CardInfo info = this._stripCardInfo(card);
				cardList.add(info);
			}
			response.setUid(chair.getUid());
			response.setObj(cardPush);
			response.setUidList(Lists.newArrayList(chair.getUid()));
			String serverID = this.getUserServerByUid(chair.getUid());
			Session session = sessionMgr.getSesion(serverID);
			session.write(response);
		}
	}

	public void autoChooseCard(String tableID) {
		TableVo table = redisDao.getTableVo(tableID);
		List<ChairVo> usedList = table.getUsedChairs();
		Map<Long, Card[]> retMap = Maps.newHashMap();

		for (ChairVo chair : usedList) {
			List<Card> cardPool = chair.getCardPool();
			if (chair.getDarkCardList().isEmpty() && cardPool.size() > 0) {
				// 暗牌随机
				int randIndex = Utils.randInt(0, cardPool.size() - 1);
				Card darkCard = cardPool.get(randIndex);
				chair.getDarkCardList().add(darkCard);
				// 名牌随机
				UserVo uvo = redisDao.getUserVoById(chair.getUid());
				Card openCard = uvo.maxNetValueCard();
				chair.getOpenCardList().add(openCard);

				chair.chooseCard(openCard, darkCard);
				retMap.put(chair.getUid(), new Card[] { openCard, darkCard });
			}
		}
		redisDao.saveTableVo(table);

		MessageRes response = new MessageRes();
		response.setCmd(ModuleCmd.autoChooseCard);
		response.setSeque(-1);

		for (Long uid : retMap.keySet()) {
			UserVo uvo = redisDao.getUserVoById(uid);
			Session session = sessionMgr.getSesion(uvo.getServerID());
			GameAutoChooseCardPush chooseCardPush = new GameAutoChooseCardPush();
			chooseCardPush.setDarkCardID(retMap.get(uid)[0].getCardID());
			chooseCardPush.setOpenCardID(retMap.get(uid)[1].getCardID());
			response.setObj(chooseCardPush);
			session.write(response);
		}

		_chooseCard(table);
	}

	private void _chooseCard(TableVo table) {

		// 推送桌子信息 进入下一阶段
		// todo
		if (table.isCompletCurStage()) {

			table.setTableOprateCount(-table.getTableOprateCount());
			table.changeGameStatus(Define.GameStatusEnum.GRAB_BAKER.getValue());
			table.setLastdoTime(TimeTool.snow());

			Map<Long, TableInfo> tableInfoMap = _stripTableInfo(table);
			List<ChairVo> usedList = table.getUsedChairs();

			MessageRes response = new MessageRes();
			response.setSeque(-1);
			response.setCmd(ModuleCmd.tablePush);

			for (int i = 0; i < usedList.size(); i++) {
				UserVo uvo = redisDao.getUserVoById(usedList.get(i).getUid());
				Session session = sessionMgr.getSesion(uvo.getServerID());
				response.setObj(tableInfoMap.get(uvo.getUid()));
				response.setUid(uvo.getUid());
				session.write(response);
			}
		}
		table.modifyTableJob(TimeTool.getDate());
		redisDao.saveTableVo(table);
	}

	public void autoGrabBanker(String tableID) {
		TableVo table = redisDao.getTableVo(tableID);
		List<ChairVo> plList = table.getPlChairs();
		for (ChairVo chair : plList) {
			if (chair.getScore() <= 0) {
				chair.grabBanker(1);
			}
		}
		redisDao.saveTableVo(table);
		this._grabBanker(tableID);
	}

	public void autoRaise(String tableID) {
		TableVo table = redisDao.getTableVo(tableID);
		List<ChairVo> plList = table.getPlChairs();
		for (ChairVo chair : plList) {
			if (chair.getSlot() != table.getBankerSlot() && chair.getBetScore() <= 0) {
				chair.raise(5);
			}
		}
		redisDao.saveTableVo(table);
		this._raise(tableID);
	}

	/**
	 * 选牌
	 * 
	 * @param param
	 */
	public void chooseCard(GameLogicEvent param) {
		GameChooseCardReq req = (GameChooseCardReq) param.getRequest().getObj();
		int uid = req.getUserID();
		UserVo uvo = redisDao.getUserVoById(uid);
		if (uvo == null) {
			logger.info("GameService chooseCard User not exsit uid::" + uid);
			param.getSession().write(param.getResponse());
			return;
		}
		String tableID = req.getTableID();
		TableVo table = redisDao.getTableVo(tableID);
		if (table == null) {
			logger.info(
					String.format("GameService chooseCard table not exsit!! %d[tableID]", new Object[] { tableID }));
			param.getSession().write(param.getResponse());
			return;
		}
		ChairVo chair = table.getChairByUid(uid);
		if (chair == null) {
			logger.info(String.format("GameService chooseCard User not in table!! %d[tableID] %d[uid]",
					new Object[] { tableID, uid }));
			param.getSession().write(param.getResponse());
			return;
		}

		if (chair.getStatus() != Define.GameStatusEnum.FREE.getValue()) {
			logger.info(String.format("GameService chooseCard table Chair Status Not Correct !! %d[tableID] %d[status]",
					new Object[] { tableID, chair.getStatus() }));
			param.getSession().write(param.getResponse());
			return;
		}

		Card openCard = uvo.getCardById(req.getOpenCardID());
		Card darkCard = chair.getDarkCardById(req.getDarkCardID());
		if (openCard == null || darkCard == null) {
			logger.info(String.format("GameService chooseCard Card Not Exsit !! %d[tableID] %d[cardID]",
					new Object[] { tableID, req.getDarkCardID() }));
			param.getSession().write(param.getResponse());
			return;

		}
		// 选择卡牌成功
		chair.chooseCard(openCard, darkCard);
		param.getSession().write(param.getResponse());

		this._chooseCard(table);

	}

	boolean checkCardExsit() {

		return false;
	}

	Map<Long, TableInfo> _stripTableInfo(TableVo table) {

		Map<Long, TableInfo> messageRet = Maps.newHashMap();
		// Map<Integer, ChairInfo> chairInfoMap = Maps.newHashMap();

		List<ChairVo> usedChairs = table.getUsedChairs();
		List<ChairVo> plList = table.getPlChairs();

		for (int i = 0; i < usedChairs.size(); i++) {

			ChairVo out_chair = usedChairs.get(i);
			TableInfo tableInfo = new TableInfo();
			List<ChairInfo> chairInfoList = Lists.newArrayList();
			for (int j = 0; j < plList.size(); j++) {
				ChairVo chair = plList.get(i);

				ChairInfo chairInfo = new ChairInfo();
				GameUserInfo userInfo = this._stripUserInfo(chair.getUid());

				chairInfo.setChairSlot(chair.getSlot());
				chairInfo.setStatus(chair.getStatus());
				// chairInfo.setOpenCard(_stripCardInfo(chair.getDarkCardList().get(0)));
				chairInfo.setUserInfo(userInfo);

				if (chair.getSlot() == out_chair.getSlot()) {
					// chairInfo.setOpenCardID(chair.getDarkCardList().get(0).getCardID());
					// chairInfo.setDarkCard(_stripCardInfo(chair.getDarkCardList().get(0)));
				}
			}

			tableInfo.setTableID(table.getTableID());
			tableInfo.setChairs(chairInfoList);

			GameTablePush push = new GameTablePush();
			push.setTableInfo(tableInfo);
			messageRet.put(out_chair.getUid(), tableInfo);
		}
		return messageRet;
	}

	GameUserInfo _stripUserInfo(long uid) {

		GameUserInfo userInfo = new GameUserInfo();

		UserVo uvo = redisDao.getUserVoById(uid);

		userInfo.setUserID(uvo.getUid());
		userInfo.setUserName(uvo.getUserName());
		userInfo.setSex(uvo.getSex());
		userInfo.setIcon(uvo.getIcon());
		userInfo.setChip(uvo.getChip());
		userInfo.setStatus(uvo.getStatus());
		return userInfo;
	}

	CardInfo _stripCardInfo(Card card) {

		CardInfo cardInfo = new CardInfo();
		cardInfo.setCardID(card.getCardID());
		cardInfo.setNetValue(card.getNetValue());
		cardInfo.setOldNetValues(card.getOldValues());

		// 行业信息
		List<Industry> cardIndustryList = card.getIndustryList();
		List<IndustryInfo> industryList = Lists.newArrayList();

		for (Industry cardIndustry : cardIndustryList) {
			IndustryInfo indutryInfo = new IndustryInfo();
			indutryInfo.setIndustryName(cardIndustry.getIndustryName());
			indutryInfo.setIndustryPercent(cardIndustry.getIndustryPercent());

			// 股票
			List<StockInfo> stockList = Lists.newArrayList();
			for (Stock stocks : cardIndustry.getStockList()) {
				StockInfo stockInfo = new StockInfo();
				stockInfo.setStockCode(stocks.getStockCode());
				stockInfo.setNowValue(stocks.getNowValue());
			}
			indutryInfo.setStockList(stockList);
		}
		cardInfo.setIndustryList(industryList);

		return cardInfo;
	}

	/**
	 * 用户所在服务器
	 * 
	 * @return
	 */
	public Map<String, List<Long>> getUsersServer(TableVo table) {

		HashMap<String, List<Long>> usersServer = Maps.newHashMap();

		for (ChairVo ch : table.getChairs()) {
			if (ch != null) {
				UserVo uvo = redisDao.getUserVoById(ch.getUid());
				String sid = uvo.getServerID();
				List<Long> uidList = usersServer.get(sid);

				if (uidList == null) {
					uidList = Lists.newArrayList();
					usersServer.put(sid, uidList);
				}
				uidList.add(uvo.getUid());
			}
		}
		return usersServer;
	}

	public String getUserServerByUid(long uid) {
		UserVo uvo = redisDao.getUserVoById(uid);
		String sid = uvo.getServerID();
		return sid;
	}

	/**
	 * 抢庄
	 * 
	 * @param param
	 */
	public void grabBanker(GameLogicEvent param) {
		GameGrabBankerReq req = (GameGrabBankerReq) param.getRequest().getObj();
		GameGrabBankerAck ack = new GameGrabBankerAck();

		String tableID = req.getTableID();
		int uid = req.getUserID();

		TableVo table = redisDao.getTableVo(tableID);
		if (table == null) {
			logger.info(
					String.format("GameService grabBanker table not exsit!! tableID:[%d]", new Object[] { tableID }));
			param.getSession().write(param.getResponse());
			return;
		}
		ChairVo chair = table.getChairByUid(uid);
		if (chair == null) {
			logger.info(String.format("GameService grabBanker User not in table!! tableID:[%d] uid:[%d]",
					new Object[] { tableID, uid }));
			param.getSession().write(param.getResponse());
			return;
		}
		if (chair.getStatus() != Define.GameStatusEnum.GRAB_BAKER.getValue()) {
			logger.info("grabBanker Status Not Corect!!");
			param.getSession().write(param.getResponse());
			return;
		}
		if (req.getScore() <= 0) {
			logger.info("grabBanker Status Not Corect!!");
			param.getSession().write(param.getResponse());
			return;
		}

		chair.grabBanker(req.getScore());
		param.getResponse().setObj(ack);
		param.getSession().write(param.getResponse());

		this._grabBanker(tableID);
	}

	void _grabBanker(String tableID) {

		TableVo table = redisDao.getTableVo(tableID);
		// 进入下一阶段
		if (table.isCompletCurStage()) {
			table.setTableOprateCount(-table.getTableOprateCount());
			table.changeGameStatus(Define.GameStatusEnum.RAISE.getValue());
			table.setLastdoTime(TimeTool.snow());
			redisDao.saveTableVo(table);

			List<ChairVo> plList = table.getPlChairs();
			ChairVo bankerChair = getBankerByScore(plList);
			table.setBankerSlot(bankerChair.getSlot());

			GameBankerRetPush push = new GameBankerRetPush();
			List<GameScoreInfo> scoreList = Lists.newArrayList();

			push.setTableID(tableID);
			push.setBakerID(bankerChair.getUid());
			push.setScoreList(scoreList);
			for (int i = 0; i < plList.size(); i++) {
				ChairVo ch = plList.get(i);
				GameScoreInfo info = new GameScoreInfo();
				info.setUserID(ch.getUid());
				info.setScore(ch.getScore());
				scoreList.add(info);
			}

			// push
			MessageRes response = new MessageRes();
			response.setCmd(ModuleCmd.bankerRetPush);
			response.setSeque(-1);
			response.setObj(push);

			Map<String, List<Long>> serverMap = this.getUsersServer(table);
			for (String serverID : serverMap.keySet()) {
				Session session = sessionMgr.getSesion(serverID);
				response.setUidList(serverMap.get(serverID));
				session.write(response);
			}
		}
		table.modifyTableJob(TimeTool.getDate());
	}

	ChairVo getBankerByScore(List<ChairVo> plChairs) {

		Collections.sort(plChairs, new Comparator<ChairVo>() {
			@Override
			public int compare(ChairVo o1, ChairVo o2) {
				// TODO Auto-generated method stub
				int ret = Integer.valueOf(o2.getScore()).compareTo(Integer.valueOf(o1.getScore()));
				if (ret == 0) {
					return Long.valueOf(o1.getLastdoTime()).compareTo(Long.valueOf(o2.getLastdoTime()));
				}
				return ret;
			}
		});
		return plChairs.get(0);
	}

	/**
	 * 下注
	 * 
	 * @param param
	 */
	public void raise(GameLogicEvent param) {
		GameRaiseReq req = (GameRaiseReq) param.getRequest().getObj();
		GameRaiseAck ack = new GameRaiseAck();

		String tableID = req.getTableID();
		long uid = req.getUserID();

		TableVo table = redisDao.getTableVo(tableID);
		if (table == null) {
			logger.info(String.format("GameService raise table not exsit!! tableID:[%d]", new Object[] { tableID }));
			param.getSession().write(param.getResponse());
			return;
		}
		ChairVo chair = table.getChairByUid(uid);
		if (chair == null) {
			logger.info(String.format("GameService raise User not in table!! tableID:[%d] uid:[%d]",
					new Object[] { tableID, uid }));
			param.getSession().write(param.getResponse());
			return;
		}

		if (chair.getStatus() != Define.GameStatusEnum.RAISE.getValue()) {
			logger.info("raise Status Not Corect!!" + chair.getStatus());
			param.getSession().write(param.getResponse());
			return;
		}

		if (chair.getSlot() == table.getBankerSlot()) {
			logger.info("Banker cannot raise Again !! uid::" + uid);
			param.getSession().write(param.getResponse());
			return;
		}

		if (req.getBetScore() <= 0) {
			logger.info("raise Status Not Corect!!");
			param.getSession().write(param.getResponse());
			return;
		}

		chair.raise(req.getBetScore());
		param.getResponse().setObj(ack);
		param.getSession().write(param.getResponse());

		this._raise(tableID);
	}

	void _raise(String tableID) {

		TableVo table = redisDao.getTableVo(tableID);

		if (table.isCompletCurStage()) {
			table.setTableOprateCount(-table.getTableOprateCount());
			redisDao.saveTableVo(table);

			List<ChairVo> plList = table.getPlChairs();
			GameRaiseRetPush push = new GameRaiseRetPush();
			List<GameScoreInfo> raiseList = Lists.newArrayList();
			push.setTableID(tableID);
			push.setRaiseList(raiseList);

			for (int i = 0; i < plList.size(); i++) {
				ChairVo ch = plList.get(i);
				if (ch.getSlot() != table.getBankerSlot()) {
					GameScoreInfo info = new GameScoreInfo();
					info.setUserID(ch.getUid());
					info.setScore(ch.getBetScore());
					raiseList.add(info);
				}
			}

			MessageRes response = new MessageRes();
			response.setCmd(ModuleCmd.raisePush);
			response.setSeque(-1);
			response.setObj(push);

			Map<String, List<Long>> userServerMap = this.getUsersServer(table);
			for (String serverID : userServerMap.keySet()) {
				Session session = sessionMgr.getSesion(serverID);
				response.setUidList(userServerMap.get(serverID));
				session.write(response);
			}
		}

		this.gameOver(tableID);
	}

	/**
	 * 结算 散户跟庄家比牌 先算庄家赢钱 后算输的钱 判断是否逃跑 逃跑扣钱 推送结果 设置等待时间3s
	 * 
	 * @param tableID
	 */
	public void gameOver(String tableID) {
		TableVo table = redisDao.getTableVo(tableID);

		if (table.getBankerSlot() < 0) {
			logger.error("gameOver Table Banker Not Exsit!!! tableID::" + tableID);
		}
		ChairVo bankerChair = table.getChairs()[table.getBankerSlot()];
		if (bankerChair == null) {
			logger.error("gameOver Table Banker Not Exsit!!! tableID::" + tableID);
		}

		Map<Integer, BalanceInfo> balenceInfoMap = Maps.newHashMap();

		List<ChairVo> plList = table.getPlChairs();

		Map<Integer, Integer> caculatelRet = Maps.newHashMap(); // 结算记录
		Map<Integer, Integer> retailLose = Maps.newHashMap(); // 散户输记录

		int bankerWin = 0;
		int bankerLose = 0;
		int bankerTableChip = 0;
		int baseScore = bankerChair.getScore() * 100;
		float bankerNetValue = cardAvgNetValue(bankerChair);

		for (ChairVo chair : plList) {
			if (chair.getSlot() != bankerChair.getSlot()) {
				float netValue = cardAvgNetValue(chair);
				int win = chair.getBetScore() * baseScore;

				if (bankerNetValue > netValue) { // 庄家赢
					bankerWin += win;
					caculatelRet.put(chair.getSlot(), -win);
				} else if (bankerNetValue < netValue) {// 散户赢
					retailLose.put(chair.getSlot(), chair.getBetScore() / 5);
				}

				BalanceInfo balanceInfo = new BalanceInfo();
				balanceInfo.setUserID(chair.getUid());
				Card darkCard = chair.getDarkCardList().get(0);
				CardInfo cardInfo = this._stripCardInfo(darkCard);
				// balanceInfo.setDarkCard(cardInfo);
				balanceInfo.setDarkCardVal(cardInfo.getNetValue());
				balenceInfoMap.put(chair.getSlot(), balanceInfo);

				bankerTableChip += win;
			}
		}

		// 预增加庄家赢的筹码
		UserVo bankerUser = redisDao.getUserVoById(bankerChair.getUid());
		float percent = 1.0f;
		int bankerTempTotal = bankerUser.getChip() + bankerWin;
		if (bankerTableChip > bankerTempTotal) {
			percent = bankerTempTotal / bankerTableChip;
		}

		for (Integer slot : retailLose.keySet()) {
			int retailWin = (int) (percent * baseScore * retailLose.get(slot));
			caculatelRet.put(slot, retailWin);
			bankerLose += retailWin;
		}

		caculatelRet.put(bankerChair.getSlot(), bankerWin - bankerLose);

		// 扣除
		for (ChairVo ch : plList) {
			UserVo uvo = redisDao.getUserVoById(ch.getUid());

			// 扣除底分
			int add = caculatelRet.get(ch.getSlot()) - baseScore;
			// 逃跑扣分
			if (ch.getStatus() == Define.GameStatusEnum.EXIT.getValue()) {
				add -= baseScore;
			}

			if (add >= 0) {
				uvo.addChip(add);
			} else {
				uvo.consumChip(baseScore);
			}

			BalanceInfo info = balenceInfoMap.get(ch.getSlot());
			info.setWinMoney(add);
			info.setTotalChip(uvo.getChip());

			redisDao.saveUserVo(uvo);
		}

		table.setStatus(Define.GameStatusEnum.BALANCE.getValue());
		redisDao.saveTableVo(table);

		// 推送结算消息
		GameBalancePush push = new GameBalancePush();
		push.setTableID(tableID);
		push.setBalenceInfoList(balenceInfoMap.values());
		sendMsg(table, push, -1, ModuleCmd.balancePush);

		table.modifyTableJob(TimeTool.getDate());
	}

	private float cardAvgNetValue(ChairVo chair) {
		float avgNetValue = (chair.getDarkCardList().get(0).getNetValue()
				+ chair.getOpenCardList().get(0).getNetValue()) / 2;
		return avgNetValue;
	}

	// 清理table数据
	private void resetGameData(String tableID) {
		TableVo table = redisDao.getTableVo(tableID);
		table.resetTableData();
		redisDao.saveTableVo(table);

		table.modifyTableJob(TimeTool.getDate());
	}

	/**
	 * 确认继续游戏
	 * 
	 * @param param
	 */
	public void gameRestartConfirm(GameLogicEvent param) {

		GameReStartConfirmReq req = (GameReStartConfirmReq) param.getRequest().getObj();
		String tableID = req.getTableID();
		long uid = req.getUserID();

		TableVo table = redisDao.getTableVo(tableID);
		if (table == null) {
			logger.info(String.format("GameService GameRestartConfirm table not exsit!! tableID:[%d]",
					new Object[] { tableID }));
			param.getSession().write(param.getResponse());
			return;
		}
		ChairVo chair = table.getChairByUid(uid);
		if (chair == null) {
			logger.info(String.format("GameService GameRestartConfirm User not in table!! tableID:[%d] uid:[%d]",
					new Object[] { tableID, uid }));
			param.getSession().write(param.getResponse());
			return;
		}

		if (table.getStatus() != Define.GameStatusEnum.FREE.getValue()) {
			logger.info("GameRestartConfirm Status Not Corect!!" + chair.getStatus());
			param.getSession().write(param.getResponse());
			return;
		}

		chair.confirmRestart(req.isReStart());
		GameReStartConfirmAck ack = new GameReStartConfirmAck();
		param.getResponse().setObj(ack);
		param.getSession().write(param.getResponse());

		this.restartGame(table);
	}

	/**
	 * 退出游戏
	 * 
	 * @param param
	 */
	public void exitGame(GameLogicEvent param) {
		GameExitReq req = (GameExitReq) param.getRequest().getObj();
		TableVo table = redisDao.getTableVo(req.getTableID());
		if (table == null) {
			logger.info(String.format("GameService exitGame table not exsit!! tableID:[%d]",
					new Object[] { req.getTableID() }));
			param.getSession().write(param.getResponse());
			return;
		}
		ChairVo chair = table.getChairByUid(req.getUid());
		if (chair == null) {
			logger.info(String.format("GameService exitGame User not in table!! tableID:[%d] uid:[%d]",
					new Object[] { req.getTableID(), req.getUid() }));
			param.getSession().write(param.getResponse());
			return;
		}

		if (chair.getStatus() == Define.GameStatusEnum.WATCHING.getValue()
				|| table.getStatus() == Define.GameStatusEnum.FREE.getValue()) {

			// 推送退出消息
			List<Long> uids = Lists.newArrayList();
			uids.add(chair.getUid());
			exitTablePush(table, uids);
			chair = null;
		} else {
			chair.setStatus(Define.GameStatusEnum.EXIT.getValue());
			chair.setLastdoTime(TimeTool.snow());
		}
		redisDao.saveTableVo(table);

		GameExitAck ack = new GameExitAck();
		ack.setPunishCD(60);
		param.getResponse().setObj(ack);
		param.getSession().write(param.getResponse());
	}

	private void exitTablePush(TableVo table, List<Long> uids) {

		MessageRes response = new MessageRes();
		response.setCmd(ModuleCmd.exitTablePush);
		response.setSeque(-1);

		GameExitTablePush push = new GameExitTablePush();
		push.setTableID(table.getTableID());
		push.setUsers(uids);

		response.setObj(push);
		Map<String, List<Long>> usersServerMap = this.getUsersServer(table);
		for (String serverID : usersServerMap.keySet()) {
			response.setUidList(usersServerMap.get(serverID));
			Session session = sessionMgr.getSesion(serverID);
			session.write(response);
		}
	}

	public void userOffLine(GameLogicEvent param) {

	}

	/**
	 * 自动确认开始 检查没确认的用户， 检查用户筹码 检查是否够开局条件？ 不够，等待并推送退出用户的id 够 ，推送选牌界面
	 * 
	 * @param tableID
	 */
	private void autoConfirmStartGame(String tableID) {
		TableVo table = redisDao.getTableVo(tableID);

		List<ChairVo> usedList = table.getUsedChairs();
		for (ChairVo chair : usedList) {
			if (chair.getStatus() == Define.GameStatusEnum.FREE.getValue()) {
				UserVo uvo = redisDao.getUserVoById(chair.getUid());
				if (uvo.getChip() < 10000) {
					chair.confirmRestart(false);
				} else {
					chair.confirmRestart(true);
				}

			}
		}
		restartGame(table);
	}

	/**
	 * 重新开始游戏
	 * 
	 * @param tableID
	 */
	private void restartGame(TableVo table) {

		List<ChairVo> usedList = table.getUsedChairs();

		if (table.isCompletCurStage()) {
			if (usedList.isEmpty()) {
				disbandTable(table.getTableID());
			} else {
				Map<Integer, ChairVo> notReadyMap = table.getNotReadyChairs();
				if (notReadyMap.size() < 3) { // 等待并推送退出用户id

					// 清理托管和退出的数据
					GameExitTablePush exitTablePush = new GameExitTablePush();
					exitTablePush.setTableID(table.getTableID());

					for (int i = 0; i < usedList.size(); i++) {
						ChairVo chair = usedList.get(i);
						UserVo uvo = redisDao.getUserVoById(chair.getUid());
						if (uvo.getStatus() == Define.UserStatusEnum.OFFLINE.getValue()
								|| notReadyMap.get(chair.getSlot()) != null) {
							chair = null;
							exitTablePush.getUsers().add(uvo.getUid());
						}
					}

					// 推送托管剔除消息
					sendMsg(table, exitTablePush, -1, ModuleCmd.exitTablePush);

					table.modifyTableJob(TimeTool.getDate());
				} else {
					this.beginCard(table.getTableID());
				}
				table.setTableOprateCount(-table.getTableOprateCount());
				redisDao.saveTableVo(table);
			}
		}
	}

	private void disbandTable(String tableID) {

		TableVo.cancelTableJob(tableID);
		redisDao.delTableVo(tableID);
	}

	// void getRaise

	// 不为空 修改状态 判断是否在游戏组 ...》
	// 根据id找房间 判断房间是否有效
	// 有效加入
	// 无效 找空房间
	// 创建房间
	// 1
	public void enterTable(UserVo uvo) {
		try {
			TableVo table = null;
			String tableID = uvo.getTableID();
			if (!StringUtil.isEmpty(tableID)) {

				table = redisDao.getTableVo(tableID);
				if (table != null) {
					boolean isInTable = table.checkUserIsInTable(uvo.getUid());
					if (!isInTable) {// 无效
						logger.info("User doesn't in the table uid:" + uvo.getUid() + " talbeID:" + tableID);
						uvo.resetUserData();
					}
				}

			}
			if (table == null) {				
				table = redisDao.getNotFullTable();
				logger.info("find a not full table tableID:"+JsonUtil.getJsonFromBean(table));
			}
			if (table == null) {
				table = TableVo.createTable();
				redisDao.saveTableVo(table);
			}

			this.joinTable(uvo.getUid(), table.getTableID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 4进入桌子
	public void joinTable(long uid, String tableID) {
		TableVo table = redisDao.getTableVo(tableID);

		int slot = table.jion(uid);
		if (slot < 0) {
			logger.error("shareTable error tableID:" + tableID + " uid:" + uid);
		}

		redisDao.saveTableVo(table);
	}

	private void sendMsg(TableVo table, Object ack, long seq, int cmd) {

		MessageRes response = new MessageRes();
		response.setCmd(cmd);
		response.setSeque(seq);
		response.setObj(ack);

		Map<String, List<Long>> usersServerMap = this.getUsersServer(table);
		for (String serverID : usersServerMap.keySet()) {
			Session session = sessionMgr.getSesion(serverID);
			response.setUidList(usersServerMap.get(serverID));
			session.write(response);
		}
	}

}
