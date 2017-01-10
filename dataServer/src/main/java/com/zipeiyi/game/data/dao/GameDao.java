package com.zipeiyi.game.data.dao;

import com.zipeiyi.game.data.model.GameFlow;

import java.util.List;

/**
 * Created by zhuhui on 16-12-14.
 */
public interface GameDao {
    void insertPlayerLastGameResult(GameFlow gameFlow);

    List<GameFlow> getPlayerGameResults(long accountId);

    void insertGameFlow(List<GameFlow> gameFlowList);
}
