package com.zipeiyi.game.data.dao.impl;

import com.zipeiyi.game.data.dao.CardDao;
import com.zipeiyi.game.data.model.*;
import com.zipeiyi.xpower.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuhui on 16-12-14.
 */
@Repository
public class CardDaoImpl implements CardDao {

    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void createCard(Card card) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO card (upid_id, hash_code, type, last_rate_value, create_time, update_time) ")
                .append("VALUES(?, ?, ?)");
        dao.insert(new OpInsert<Integer>(sql, bizName, Integer.class).addParams(card.getUpidId(), card.getHashCode(), card.getType(), card.getLastRateValue(), new Date(), null));

    }

    @Override
    public void createCards(List<Card> cardSet) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO card ");
        sql.append("(upid_id, hash_code,upid_json, type, last_rate_value,rate_values, create_time, update_time) ");
        sql.append("VALUES ( ?, ?, ?, ?, ?, ?, ?, null) ");
        dao.batchUpdate(new OpUpdate(sql, bizName) {
            @Override
            protected void setParams(PreparedStatement ps) throws SQLException {
                for (Card card : cardSet) {
                    ps.setString(1, card.getUpidId());
                    ps.setString(2, card.getHashCode());
                    ps.setString(3, card.getUpidJson());
                    ps.setString(4, card.getType().toString());
                    ps.setBigDecimal(5, card.getLastRateValue());
                    ps.setString(6, card.getRateValues());
                    ps.setTimestamp(7, new java.sql.Timestamp(card.getCreateTime().getTime()));
                    ps.addBatch();
                }
            }
        });
    }

    @Override
    public void createUpid(Upid upid) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO upid ")
                .append("(upid_id, hash_code, create_date ) ")
                .append("VALUES(?, ?, ?) ");
        dao.insert(new OpInsert<>(sql, bizName, Integer.class).addParams(upid.getUpidId(), upid.getHashCode(), upid.getCreateDate()));
        //this.createUpidBroads(upid.getBroadList());


    }

    private void createUpidBroads(List<Broad> broadList) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO broad ");
        sql.append("(upid_id, broad_id, broad_name, broad_proportion) ");
        sql.append("VALUES ( ?, ?, ?, ?) ");
        dao.batchUpdate(new OpUpdate(sql, bizName) {
            @Override
            protected void setParams(PreparedStatement ps) throws SQLException {
                for (Broad broad : broadList) {
                    ps.setString(1, broad.getUpidId());
                    ps.setString(2, broad.getBroadId());
                    ps.setString(3, broad.getBroadName());
                    ps.setString(4, broad.getBroadProportion());
                    ps.addBatch();
                    createUpidBroadStocks(broad.getStockList());
                }
            }
        });
    }

    private void createUpidBroadStocks(List<Stock> stockList) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO stock ");
        sql.append("(upid_id, broad_id, sec_id, sec_name) ");
        sql.append("VALUES ( ?, ?, ?, ?) ");
        dao.batchUpdate(new OpUpdate(sql, bizName) {
            @Override
            protected void setParams(PreparedStatement ps) throws SQLException {
                for (Stock stock : stockList) {
                    ps.setString(1, stock.getUpidId());
                    ps.setString(2, stock.getBroadId());
                    ps.setString(3, stock.getSecId());
                    ps.setString(4, stock.getSecName());
                    ps.addBatch();
                }
            }
        });
    }

    @Override
    public void updateLastRateValue(Card card) {

    }

    @Override
    public void updateBatchRateValues(Map<String, BigDecimal> upidRateValueMap) {
        if (upidRateValueMap != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE card ");
            sql.append("SET rate_value=CASE upid_id ");
            Iterator<Map.Entry<String, BigDecimal>> map = upidRateValueMap.entrySet().iterator();
            while (map.hasNext()) {
                Map.Entry<String, BigDecimal> entry = map.next();
                sql.append("WHEN " + entry.getKey() + " THEN " + entry.getValue());
            }
            sql.append("END ");
            dao.update(new OpUpdate(sql, bizName));
        }
    }

    @Override
    public List<Card> getCards() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ")
                .append("card ");
        return dao.queryList(new DefaultOpList<>(sql, bizName, new CardMapper()));
    }

    @Override
    public Integer cardCountUnused() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(id) AS count FROM card ")
                .append("WHERE id not in ")
                .append("(SELECT card_id FROM  ")
                .append("user_r2_card) ");
        return dao.queryUniq(new DefaultOpUniq<Integer>(sql, bizName).setMapper((resultSet, i) -> resultSet.getInt("count")));
    }

    @Override
    public Card getUnusedCard() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM card ")
                .append("WHERE ")
                .append("used <> 1 ")
                .append("LIMIT 1 ");
        return dao.queryUniq(new DefaultOpUniq<Card>(sql, bizName).setMapper((resultSet, i) -> {
                    Card card = new Card();
                    card.setId(resultSet.getLong("id"));
                    card.setUpidId(resultSet.getString("upid_id"));
                    return card;
                }
        ));
    }

    @Override
    public List<Card> getUnusedCards() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM card ")
                .append("WHERE ")
                .append("used <> 1 ")
                .append("LIMIT 3 ");
        return dao.queryList(new DefaultOpList<Card>(sql, bizName, (resultSet, i) -> {
            Card card = new Card();
            card.setId(resultSet.getLong("id"));
            card.setUpidId(resultSet.getString("upid_id"));
            return card;
        }));
    }

    @Override
    public List<Card> getUsedCards() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM card ")
                .append("WHERE ")
                .append("type = 'TYPE_1' ")
                .append("AND used = 1 ");
        return dao.queryList(new DefaultOpList<Card>(sql, bizName, (resultSet, i) -> getResult2Card(resultSet)));
    }

    @Override
    public void updateCardLastRateValue(List<Card> cardList) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE card ")
                .append("SET upid_json = ? ")
                .append(", last_rate_value = ? ")
                .append(", rate_values = ? ")
                .append(", update_time = ? ")
                .append("WHERE id = ? ");
        dao.batchUpdate(new OpUpdate(sql, bizName) {
            @Override
            protected void setParams(PreparedStatement ps) throws SQLException {
                for (Card card : cardList) {
                    ps.setString(1, card.getUpidJson());
                    ps.setBigDecimal(2, card.getLastRateValue());
                    ps.setString(3, card.getRateValues());
                    ps.setTimestamp(4, new java.sql.Timestamp(card.getUpdateTime().getTime()));
                    ps.setLong(5, card.getId());
                    ps.addBatch();
                }
            }
        });
    }

    @Override
    public List<Card> getUserCards(long uid) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM card c ")
                .append("LEFT JOIN user_r2_card r ")
                .append("ON c.id = r.card_id ")
                .append("WHERE ")
                .append("r.user_id = ? ");
        return dao.queryList(new DefaultOpList<>(sql, bizName, (resultSet, i) -> getResult2Card(resultSet)).addParams(uid));
    }

    @Override
    public List<Card> getHideCards() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM card ")
                .append("WHERE ")
                .append("type = 'TYPE_2' ");
        return dao.queryList(new DefaultOpList<>(sql, bizName, (resultSet, i) -> getResult2Card(resultSet)));
    }

    private Card getResult2Card(ResultSet resultSet) throws SQLException {
        Card card = new Card();
        card.setId(resultSet.getLong("id"));
        card.setUpidId(resultSet.getString("upid_id"));
        card.setHashCode(resultSet.getString("hash_code"));
        card.setUpidJson(resultSet.getString("upid_json"));
        card.setLastRateValue(resultSet.getBigDecimal("last_rate_value"));
        card.setRateValues(resultSet.getString("rate_values"));
        card.setType(CardTypeEnum.valueOf(resultSet.getString("type")));
        card.setUsed(resultSet.getBoolean("used"));
        card.setCreateTime(resultSet.getDate("create_time"));
        card.setUpdateTime(resultSet.getDate("update_time"));
        return card;
    }

    @Override
    public void markUsedCard(Card unusedCard) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE card ")
                .append("SET used = 1 ")
                .append("WHERE id = ? ");
        dao.update(new OpUpdate(sql, bizName).addParams(unusedCard.getId()));
    }

    private class CardMapper implements IRowMapper<Card> {
        @Override
        public Card mapRow(ResultSet resultSet, int i) throws SQLException {
            Card card = new Card();
            card.setUpidId(resultSet.getString("upid_id"));
            return null;
        }
    }
}
