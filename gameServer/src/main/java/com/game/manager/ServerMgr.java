package com.game.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.constant.Define;
import com.game.db.dao.CenterDBDao;
import com.game.db.redis.RedisDao;
import com.game.db.redis.impl.RedisDaoImp;
import com.game.module.TexasHoldemServerImp;
import com.game.vo.Card;
import com.game.vo.ChairVo;
import com.game.vo.Industry;
import com.game.vo.Stock;
import com.game.vo.TableVo;
import com.game.vo.UserVo;
import com.google.common.collect.Lists;
import com.zipeiyi.game.common.proto.DBCardListAck;
import com.zipeiyi.game.common.proto.DBUserInfoAck;
import com.zipeiyi.game.common.proto.pojo.CardInfo;
import com.zipeiyi.game.common.proto.pojo.IndustryInfo;
import com.zipeiyi.game.common.proto.pojo.StockInfo;

public class ServerMgr {

	private static final Logger logger = LoggerFactory.getLogger(ServerMgr.class);
	private static ServerMgr instance = new ServerMgr();
	RedisDao redisDao = RedisDaoImp.getInstance();
	CenterDBDao centerDao = CenterDBDao.getInstance();

	public static ServerMgr getInstance() {
		return instance;
	}

	public UserVo addUser(String channelId, long uid) {
		// 向centerDB获取数据
		centerDao.getUserInfoByUid(uid);
		UserVo uvo = new UserVo();
		uvo.setUid(uid);
		uvo.setServerID(channelId);
		uvo.setStatus(Define.UserStatusEnum.LOADING.getValue());
		redisDao.saveUserVo(uvo);
		return uvo;
	}

	public void initUserVo(long uid, DBUserInfoAck ack) {

		UserVo uvo = getUserById(uid);
		if (uvo == null) {
			uvo = new UserVo();
		}
		uvo.setUid(uid);
		uvo.setUserName(ack.getUserInfo().getUserName());
		uvo.setIcon(ack.getUserInfo().getIcon());
		uvo.setChip(ack.getUserInfo().getChip());
		uvo.setSex(ack.getUserInfo().getSex());
		uvo.setStatus(Define.UserStatusEnum.ONLINE.getValue());

		redisDao.saveUserVo(uvo);
	}

	public void initTableCardList(String tableID, DBCardListAck ack) {
		TableVo table = redisDao.getTableVo(tableID);

		logger.info("initTableCardList DB获取的数据 初始化到table玩家====" + table + " 卡牌长度:::" + ack.getCardList().size());
		if (table != null && !table.isGaming) {//

			List<ChairVo> usedList = table.getUsedChairs();
			int size = usedList.size();
			int amount = ack.getCardList().size() / size;
			int begin = 0;
			int end = begin + amount;

			List<CardInfo> infoList = ack.getCardList();
			for (int i = begin; i < infoList.size(); i++) {
				if (i / 3 >= usedList.size()) {
					break;
				}
				ChairVo chair = usedList.get(i / 3);

//				for (int index = 0; index < 3; index++) {
//					CardInfo info = infoList.get(index);
//					Card card = _stripCard(info);
//					chair.getCardPool().add(card);
//				}
				if(chair.getCardPool().size()<3){
					CardInfo info = infoList.get(i);
					Card card = _stripCard(info);
					chair.getCardPool().add(card);
				}				
			}
			redisDao.saveTableVo(table);
			TexasHoldemServerImp.getInstance().cardPush(tableID);
		}

	}

	Card _stripCard(CardInfo cardInfo) {
		Card card = new Card();
		card.setCardID(cardInfo.getCardID());
		card.setNetValue(cardInfo.getNetValue());
		card.setOldValues(cardInfo.getOldNetValues());

		// 行业信息
		List<IndustryInfo> infoIndustryList = cardInfo.getIndustryList();
		List<Industry> cardIndustryList = Lists.newArrayList();
		for (IndustryInfo dusInfo : infoIndustryList) {
			Industry dustry = new Industry();
			List<Stock> stockList = Lists.newArrayList();

			dustry.setIndustryName(dusInfo.getIndustryName());
			dustry.setIndustryPercent(dusInfo.getIndustryPercent());
			dustry.setStockList(stockList);

			// 股票
			for (StockInfo stockInfo : dusInfo.getStockList()) {
				Stock stock = new Stock();
				stock.setStockCode(stockInfo.getStockCode());
				stock.setNowValue(stockInfo.getNowValue());
				stockList.add(stock);
			}

			cardIndustryList.add(dustry);
		}
		card.setIndustryList(cardIndustryList);
		return card;
	}

	public UserVo getUserById(long uid) {

		UserVo uvo = redisDao.getUserVoById(uid);
		if (uvo == null) {
			logger.info("从Redis获取UserVo 数据为空！！！！uid::" + uid);
			return null;
		}
		return uvo;
	}

}
