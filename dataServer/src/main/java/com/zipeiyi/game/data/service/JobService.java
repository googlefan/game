package com.zipeiyi.game.data.service;

import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.util.DateUtil;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.dao.CardDao;
import com.zipeiyi.game.data.dao.impl.CardDaoImpl;
import com.zipeiyi.game.data.model.Card;
import com.zipeiyi.game.data.model.CardTypeEnum;
import com.zipeiyi.game.data.utils.HttpClientSend;
import com.zipeiyi.game.data.utils.PostParameter;
import com.zipeiyi.game.data.utils.Response;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhuhui on 16-12-21.
 */
@Service
public class JobService {
    public static final String Redis_Field_JSON = "json";
    public static final String Redis_Field_LAST_RATE_VALUE = "lastRateValue";
    public static final String Redis_Field_RATE_VALUES = "rateValues";
    public static final String Redis_Key_UPID = "upid";
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 每天计算用户固有卡牌对应的最新净值
     */
    public void updateLastRateVlaue() throws ParseException {
        logger.info("update last rateValue every day in 00:00:00");
        SimpleDateFormat sf = new SimpleDateFormat(Constants.formatYmd);
        String today = sf.format(new Date());
        CardDao cardDao = NettyServerStart.factory.getBean(CardDaoImpl.class);
        // 获取用户拥有upid 所有的股票信息
        logger.info("************已使用明牌更新净值*****************");
        List<Card> cardList = cardDao.getUsedCards();
        for (int i = 0; i < cardList.size(); i++) {
            JSONObject upidObj = JSONObject.fromObject(cardList.get(i).getUpidJson());
            Card card = getLastRateValue(today, upidObj, CardTypeEnum.TYPE_1, new Date());
            cardList.get(i).setUpidJson(card.getUpidJson());
            cardList.get(i).setLastRateValue(card.getLastRateValue());
            cardList.get(i).setRateValues(card.getRateValues());
            cardList.get(i).setUpdateTime(card.getUpdateTime());
        }
        cardDao.updateCardLastRateValue(cardList);
        logger.info("************暗牌更新净值*****************");
        RedisService redisService = NettyServerStart.factory.getBean(RedisService.class);
        List<Card> hideCardList = cardDao.getHideCards();
        for (int i = 0; i < hideCardList.size(); i++) {
            JSONObject upidObj = JSONObject.fromObject(hideCardList.get(i).getUpidJson());
            Card card = getLastRateValue(today, upidObj, CardTypeEnum.TYPE_2, new Date());
            redisService.getRedis().hset(card.getUpidId(), Redis_Field_JSON, card.getUpidJson());
            redisService.getRedis().hset(card.getUpidId(), Redis_Field_LAST_RATE_VALUE, card.getLastRateValue().toString());
            redisService.getRedis().hset(card.getUpidId(), Redis_Field_RATE_VALUES, card.getRateValues().toString());

            hideCardList.get(i).setUpidJson(card.getUpidJson());
            hideCardList.get(i).setLastRateValue(card.getLastRateValue());
            hideCardList.get(i).setRateValues(card.getRateValues());
            hideCardList.get(i).setUpdateTime(card.getUpdateTime());
        }
        cardDao.updateCardLastRateValue(hideCardList);
        logger.info("卡牌净值更新完毕！");
    }

    /**
     * 定时获取数量为n的明牌信息存储到数据库
     */
    public void randomCardDb() throws ParseException {
        CardDao cardDao = NettyServerStart.factory.getBean(CardDaoImpl.class);
        // 获取card中未被用户选中的数量，小于100个时，再生产100个
        Integer countUnused = cardDao.cardCountUnused();
        if (countUnused > 100) {
            return;
        }
        long startTime = System.currentTimeMillis();
        logger.info("unused card count is " + countUnused + "start produce 100 cards At :" + startTime);
        Response res = HttpClientSend.getHttpClientSend().get("http://192.169.10.19:8066/UPIDInfo",
                new PostParameter[]{new PostParameter("nums", "100")}
        );
        SimpleDateFormat sf = new SimpleDateFormat(Constants.formatYmd);
        String today = sf.format(new Date());

        List<Card> cardSet = new ArrayList<>();
        JSONObject resObj = res.asJSONObject();
        JSONArray upidArr = (JSONArray) resObj.get("upids");
        Iterator upidIte = upidArr.iterator();

        while ((upidIte.hasNext())) {
            JSONObject upidObj = (JSONObject) upidIte.next();
            Card card = getLastRateValue(today, upidObj, CardTypeEnum.TYPE_1, null);
            cardSet.add(card);

            /**************缓存至Redis*******************/
            //redisService.setKeyValueTime(upidId, card.getLastRateValue().toString(), 200);
        }
        cardDao.createCards(cardSet);
        /**************标记已使用upid*******************/
        StringBuffer upidIdSb = new StringBuffer();
        for (int i = 0; i < cardSet.size(); i++) {
            upidIdSb.append(cardSet.get(i).getUpidId()).append(",");
        }
        String upidIdStr = upidIdSb.toString();
        upidIdStr = upidIdStr.substring(0, upidIdStr.length() - 1);
        HttpClientSend.getHttpClientSend().get("http://10.0.10.76:8765/usedupid",
                new PostParameter[]{
                        new PostParameter("upidid", upidIdStr)
                }
        );
        long endTime = System.currentTimeMillis();
        logger.info("produce 100 cards end at :" + endTime + "; Total cost  " + (endTime - startTime) / 1000 + "s");
    }

    private Card getLastRateValue(String today, JSONObject upidObj, CardTypeEnum type, Date updateTime) throws ParseException {
        String upidId = upidObj.getString("upidId");
        String hashCode = upidObj.getString("hashCode");
        String createDate = upidObj.getString("createDate");
        createDate = createDate.replace("-", "");
        createDate = createDate.substring(0, Constants.formatYmd.length());
        String dateBefore = DateUtil.getDateBefore(today, createDate, 30);

        JSONArray broadArr = upidObj.getJSONArray("broadList");
        Iterator broadIte = broadArr.iterator();
        StringBuffer stockStrList = new StringBuffer();
        while (broadIte.hasNext()) {
            JSONObject broadObj = (JSONObject) broadIte.next();
            JSONArray stockArr = broadObj.getJSONArray("stockList");
            Iterator stockIte = stockArr.iterator();
            while (stockIte.hasNext()) {
                JSONObject stockInfo = (JSONObject) stockIte.next();
                String stockCode = stockInfo.getString("secId");
                BigDecimal[] stockNowPrice = getStockPriceSum(today, today, stockCode);
                stockInfo.put("price", stockNowPrice[0]);
                stockStrList.append(stockCode).append(",");
            }
        }
        String stockParm = stockStrList.toString();
        stockParm = stockParm.substring(0, stockStrList.length() - 1);
        BigDecimal[] startStockPriceSum = getStockPriceSum(createDate, createDate, stockParm);
        BigDecimal[] stockPriceSumArr = getStockPriceSum(dateBefore, today, stockParm);

        StringBuffer rateValues = new StringBuffer();
        for (int i = 0; i < stockPriceSumArr.length; i++) {
            rateValues.append(stockPriceSumArr[i].divide(startStockPriceSum[0], 4, RoundingMode.HALF_UP)).append(",");
        }
        Card card = new Card();
        card.setUpidId(upidId);
        card.setHashCode(hashCode);
        card.setUpidJson(upidObj.toString());
        card.setType(type);
        card.setUsed(false);
        card.setLastRateValue(stockPriceSumArr[stockPriceSumArr.length - 1].divide(startStockPriceSum[0], 4, RoundingMode.HALF_UP));
        card.setRateValues(rateValues.substring(0, rateValues.length() - 1));
        card.setCreateTime(new Date());
        card.setUpdateTime(updateTime);
        return card;
    }

    /**
     * 定时获取数量为n暗牌的信息存储到redis
     */
    public void randomCardRedis() throws ParseException {

        long startTime = System.currentTimeMillis();
        logger.info("start produce 1000 暗牌 cards At :" + startTime);
        RedisService redisService = NettyServerStart.factory.getBean(RedisService.class);
        CardDao cardDao = NettyServerStart.factory.getBean(CardDaoImpl.class);
        Response res = HttpClientSend.getHttpClientSend().get("http://192.169.10.19:8066/UPIDInfo",
                new PostParameter[]{new PostParameter("nums", "10")}
        );
        SimpleDateFormat sf = new SimpleDateFormat(Constants.formatYmd);
        String today = sf.format(new Date());

        List<Card> cardSet = new ArrayList<>();
        JSONObject resObj = res.asJSONObject();
        JSONArray upidArr = (JSONArray) resObj.get("upids");
        Iterator upidIte = upidArr.iterator();

        while ((upidIte.hasNext())) {
            JSONObject upidObj = (JSONObject) upidIte.next();
            Card card = getLastRateValue(today, upidObj, CardTypeEnum.TYPE_2, null);
            cardSet.add(card);

            /**************缓存至Redis*******************/
            redisService.getRedis().hset(card.getUpidId(), Redis_Field_JSON, card.getUpidJson().getBytes());
            redisService.getRedis().hset(card.getUpidId(), Redis_Field_LAST_RATE_VALUE, card.getLastRateValue().toString());
            redisService.getRedis().hset(card.getUpidId(), Redis_Field_RATE_VALUES, card.getRateValues().toString());
            redisService.getRedis().sadd(Redis_Key_UPID, card.getUpidId());
        }
        cardDao.createCards(cardSet);
        /**************标记已使用upid*******************/
        StringBuffer upidIdSb = new StringBuffer();
        for (int i = 0; i < cardSet.size(); i++) {
            upidIdSb.append(cardSet.get(i).getUpidId()).append(",");
        }
        String upidIdStr = upidIdSb.toString();
        upidIdStr = upidIdStr.substring(0, upidIdStr.length() - 1);
        HttpClientSend.getHttpClientSend().get("http://192.169.10.19:8765/usedupid",
                new PostParameter[]{
                        new PostParameter("upidid", upidIdStr)
                }
        );
        long endTime = System.currentTimeMillis();
        logger.info("produce 1000 暗牌 cards end at :" + endTime + "; Total cost  " + (endTime - startTime) / 1000 + "s");

    }

    private BigDecimal[] getStockPriceSum(String start, String end, String stockParm) throws ParseException {
        Response stockPrice = HttpClientSend.getHttpClientSend().get("http://10.0.150.5:5441/stock",
                new PostParameter[]{
                        new PostParameter("code", stockParm),
                        new PostParameter("start", start),
                        new PostParameter("end", end)
                }
        );
        JSONObject stockPriceObj = stockPrice.asJSONObject();
        int days = ((JSONArray) ((JSONObject) ((JSONObject) stockPriceObj.get("results")).get(stockParm.split(",")[0])).get("price")).size();
        BigDecimal[] backArr = new BigDecimal[days];
        for (int i = 0; i < days; i++) {
            BigDecimal stockPriceSum = BigDecimal.ZERO;
            for (String stockCode : stockParm.split(",")) {
                JSONArray startStockPriceArr = (JSONArray) ((JSONObject) ((JSONObject) stockPriceObj.get("results")).get(stockCode)).get("price");
                BigDecimal startClosePrice = BigDecimal.valueOf(((JSONObject) startStockPriceArr.get(i)).getDouble("close"));
                BigDecimal startRatioAdjustingFactor = BigDecimal.valueOf(((JSONObject) startStockPriceArr.get(i)).getDouble("ratioAdjustingFactor"));
                stockPriceSum = stockPriceSum.add(startClosePrice.multiply(startRatioAdjustingFactor));
            }
            backArr[i] = stockPriceSum;
        }
        return backArr;
    }
}
