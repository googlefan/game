package com.game.module;

import java.util.HashMap;
import java.util.Map;

import com.game.event.GameLogicEvent;

public interface TexasHoldemServer {

	public void runTableJob(Map<String, Object> data);
	
	public void chooseCard(GameLogicEvent param);
	
	public void grabBanker(GameLogicEvent param) ;
	public void raise(GameLogicEvent param);
	
	public void gameRestartConfirm(GameLogicEvent param);
	
	public void exitGame(GameLogicEvent param);
	void gameOver(String tableID) ;
	
}
