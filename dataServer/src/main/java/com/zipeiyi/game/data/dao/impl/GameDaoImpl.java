package com.zipeiyi.game.data.dao.impl;

import com.zipeiyi.game.data.dao.GameDao;
import com.zipeiyi.game.data.model.GameFlow;
import com.zipeiyi.xpower.dao.DaoFactory;
import com.zipeiyi.xpower.dao.IDao;
import com.zipeiyi.xpower.dao.OpInsert;
import com.zipeiyi.xpower.dao.OpUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhuhui on 16-12-14.
 */
@Repository
public class GameDaoImpl implements GameDao {
    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(GameDaoImpl.class);

    @Override
    public void insertPlayerLastGameResult(GameFlow gameFlow) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO card (upid_id, is_dealer, win_chips, double_value, open_upid, hide_upid, create_time) ")
                .append("VALUES(?, ?, ?)");
        dao.insert(new OpInsert<Integer>(sql, bizName, Integer.class).addParams());

    }

    @Override
    public List<GameFlow> getPlayerGameResults(long accountId) {
        return null;
    }

    @Override
    public void insertGameFlow(List<GameFlow> gameFlowList) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO game_flow ")
                .append("(user_id, is_dealer, win_chips, double_value, open_upid, hide_upid, create_time) ")
                .append("VALUES(?, ?, ?, ?, ?, ?, NOW) ");
        dao.batchUpdate(new OpUpdate(sql, bizName) {
            @Override
            protected void setParams(PreparedStatement ps) throws SQLException {
                for (GameFlow gameFlow : gameFlowList) {
                    ps.setLong(1, gameFlow.getUserId());
                    ps.setBoolean(2, gameFlow.isDealer());
                    ps.setInt(3, gameFlow.getWinChips());
                    ps.setInt(4, gameFlow.getDoubleValue());
                    ps.setString(5, gameFlow.getOpenUpid());
                    ps.setString(6, gameFlow.getHideUpid());
                    ps.addBatch();
                }
            }
        });
    }
}
