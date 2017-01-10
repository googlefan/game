package com.game.service;

import com.game.data.RoomDataProvider;

/**
 * load configdata form json to cache
 * @author tpp
 *
 */
public class GameConfigDataLoadService {

	private  static GameConfigDataLoadService instance;
//	private 
	
	public static GameConfigDataLoadService getInstance(){
		
		if(instance == null){
			instance = new GameConfigDataLoadService();
		}
		return instance;
	}
	
	public void registerConfLoader(){
		RoomDataProvider.getInstance().init(null);
	}
	
}
