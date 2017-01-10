package com.game.listener;

import com.game.db.redis.RedisDao;
import com.game.event.Event;
import com.game.module.TexasHoldemServerImp;

public abstract class ITableEnventListener implements ILogicEventListener {
	
	private RedisDao redisDao;
	private TexasHoldemServerImp gameService;
	public RedisDao getRedisDao() {
		return redisDao;
	}
	public void setRedisDao(RedisDao redisDao) {
		this.redisDao = redisDao;
	}
	
	
	public TexasHoldemServerImp getGameService() {
		return gameService;
	}
	public void setGameService(TexasHoldemServerImp gameService) {
		this.gameService = gameService;
	}
	public abstract void addToQueue(Event event);

	
}
