package com.zipeiyi.game.data.service;

import com.zipeiyi.game.common.proto.pojo.CardInfo;
import com.zipeiyi.game.common.proto.pojo.IndustryInfo;
import com.zipeiyi.game.common.proto.pojo.StockInfo;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.dao.CardDao;
import com.zipeiyi.game.data.dao.UserDao;
import com.zipeiyi.game.data.dao.impl.CardDaoImpl;
import com.zipeiyi.game.data.dao.impl.UserDaoImpl;
import com.zipeiyi.game.data.model.Card;
import com.zipeiyi.game.data.model.UserR2Card;
import com.zipeiyi.game.data.utils.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhuhui on 16-12-14.
 */
@Service
public class UpidService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用户注册赠送3张卡牌
     * @param uid
     */
    public List<Card> regGiftUpid(long uid) throws ParseException {
        logger.info("quatrz run!");
        CardDao cardDao = NettyServerStart.factory.getBean(CardDaoImpl.class);
        UserDao userDao = NettyServerStart.factory.getBean(UserDaoImpl.class);
        List<Card> unUsedCards = cardDao.getUnusedCards();
        for (Card unUsedCard : unUsedCards) {
            UserR2Card userR2Card = new UserR2Card();
            userR2Card.setUserId(uid);
            userR2Card.setCardId(unUsedCard.getId());
            userR2Card.setCreateTime(new Date());
            userDao.createUserR2Card(userR2Card);
            unUsedCard.setUsed(true);
            cardDao.markUsedCard(unUsedCard);
        }
        return unUsedCards;
    }

    public List<IndustryInfo> getIndustry(String upidJson) {
        JSONObject upidObj = JSONObject.fromObject(upidJson);
        List<IndustryInfo> industryInfoList = new ArrayList<>();
        JSONArray broadArr = upidObj.getJSONArray("broadList");
        Iterator broadIte = broadArr.iterator();
        while (broadIte.hasNext()) {
            IndustryInfo industryInfo = new IndustryInfo();
            List<StockInfo> stockInfoList = new ArrayList<>();
            JSONObject broadObj = (JSONObject) broadIte.next();
            String broadName = broadObj.getString("broadName");
            String broadProportion = broadObj.getString("broadProportion");
            JSONArray stockArr = broadObj.getJSONArray("stockList");
            industryInfo.setIndustryName(broadName);
            industryInfo.setIndustryPercent(Float.valueOf(broadProportion));
            Iterator stockIte = stockArr.iterator();
            while (stockIte.hasNext()) {
                JSONObject stockObj = (JSONObject) stockIte.next();
                String stockCode = stockObj.getString("secId");
                String stockName = stockObj.getString("secName");
                String stockPrice = stockObj.getString("price");
                StockInfo stockInfo = new StockInfo();
                stockInfo.setStockCode(stockCode);
                stockInfo.setStockName(stockName);
                stockInfo.setNowValue(Float.valueOf(stockPrice));
                stockInfoList.add(stockInfo);
            }
            industryInfo.setStockList(stockInfoList);
            industryInfoList.add(industryInfo);
        }
        return industryInfoList;
    }

    public List<CardInfo> castCard2CardInfo(List<Card> cardList) {
        List<CardInfo> cardInfoList = new ArrayList<>();
        for (Card card : cardList) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setCardID(card.getUpidId());
            cardInfo.setNetValue(card.getLastRateValue().floatValue());
            cardInfo.setOldNetValues(StringUtils.getFloatFromString(card.getRateValues()));
            cardInfo.setIndustryList(getIndustry(card.getUpidJson()));
            cardInfoList.add(cardInfo);
        }
        return cardInfoList;
    }
}
