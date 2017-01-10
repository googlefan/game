package com.zipeiyi.game.data.service;

import com.zipeiyi.game.common.proto.pojo.BalanceDBInfo;
import com.zipeiyi.game.common.proto.pojo.CardInfo;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.dao.CardDao;
import com.zipeiyi.game.data.dao.GameDao;
import com.zipeiyi.game.data.dao.impl.CardDaoImpl;
import com.zipeiyi.game.data.dao.impl.GameDaoImpl;
import com.zipeiyi.game.data.model.GameFlow;
import com.zipeiyi.game.data.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuhui on 16-12-29.
 */
@Service
public class GameService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void insertGameFlow(List<BalanceDBInfo> balanceDBInfoList) {
        GameDao gameDao = NettyServerStart.factory.getBean(GameDaoImpl.class);
        List<GameFlow> gameFlowList = new ArrayList<>();
        for (BalanceDBInfo balanceDBInfo : balanceDBInfoList) {
            GameFlow gameFlow = new GameFlow();
            gameFlow.setUserId(balanceDBInfo.getUserID());
            gameFlow.setDealer(Boolean.valueOf(balanceDBInfo.getIsBanker().toString()));
            gameFlow.setDoubleValue(balanceDBInfo.getBetScore());
            gameFlow.setWinChips(balanceDBInfo.getWinMoney());
            gameFlow.setOpenUpid(StringUtils.covertToString(balanceDBInfo.getOpenCardList()));
            gameFlow.setHideUpid(StringUtils.covertToString(balanceDBInfo.getDarkCardList()));
            gameFlowList.add(gameFlow);
        }
        gameDao.insertGameFlow(gameFlowList);
    }

    /**
     * 获取固定数量的暗牌
     *
     * @param cardAmount
     * @return
     */
    public List<CardInfo> getHideCard(int cardAmount) {
        RedisService redisService = NettyServerStart.factory.getBean(RedisService.class);
        UpidService upidService = NettyServerStart.factory.getBean(UpidService.class);
        List<CardInfo> cardInfoList = new ArrayList<>();
        String tmpRan = "";
        for (int i = 0; i < cardAmount; i++) {
            String upidRandom = redisService.getRedis().srandmember(JobService.Redis_Key_UPID);
            while (tmpRan.contains(upidRandom)) {
                String tmp = upidRandom;
                upidRandom = redisService.getRedis().srandmember(JobService.Redis_Key_UPID);
                tmpRan.replace(tmp, upidRandom);
            }
            tmpRan = tmpRan.concat(upidRandom).concat(",");
            String jsonStr = redisService.getRedis().hget(upidRandom, JobService.Redis_Field_JSON);
            String lastRateValue = redisService.getRedis().hget(upidRandom, JobService.Redis_Field_LAST_RATE_VALUE);
            String rateValues = redisService.getRedis().hget(upidRandom, JobService.Redis_Field_RATE_VALUES);
            CardInfo cardInfo = new CardInfo();
            cardInfo.setCardID(upidRandom);
            cardInfo.setNetValue(Float.valueOf(lastRateValue));
            cardInfo.setOldNetValues(StringUtils.getFloatFromString(rateValues));
            cardInfo.setIndustryList(upidService.getIndustry(jsonStr));
            cardInfoList.add(cardInfo);
        }
        return cardInfoList;
    }

    /**
     * 获取用户卡牌信息
     *
     * @param uid
     * @param cardID
     * @return
     */
    public CardInfo getUserCard(Integer uid, String cardID) {
        CardDao cardDao = NettyServerStart.factory.getBean(CardDaoImpl.class);
        //List<Card> cardList = cardDao.getUserCard(uid, cardID);
        CardInfo cardInfo = new CardInfo();
        return cardInfo;
    }


}
