package com.zipeiyi.game.data.dao;

import com.zipeiyi.game.data.model.Card;
import com.zipeiyi.game.data.model.Upid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhuhui on 16-12-14.
 */
public interface CardDao {
    void createCard(Card card);

    void createCards(List<Card> cardSet);

    void createUpid(Upid upid);

    void updateLastRateValue(Card card);

    void updateBatchRateValues(Map<String, BigDecimal> upidRateValueMap);

    List<Card> getCards();

    Integer cardCountUnused();

    Card getUnusedCard();

    void markUsedCard(Card unusedCard);

    List<Card> getUnusedCards();

    List<Card> getUsedCards();

    void updateCardLastRateValue(List<Card> cardList);

   // List<Card> getUserCard(Integer uid, String cardID);

    List<Card> getUserCards(long uid);

    List<Card> getHideCards();
}
