package com.game.listener;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.constant.LogicEventType;
import com.game.event.Event;
import com.game.event.EventEngine;
import com.game.event.GameLogicEvent;
import com.google.common.collect.Maps;

/**
 * 
 * @author tpp
 *  
 */
public class TableCreateEventListener extends ITableEnventListener{

	private static final Logger logger = LoggerFactory.getLogger(TableCreateEventListener.class);

	private HashMap<String, GameLogicEvent> map = Maps.newHashMap();
	
	private static TableCreateEventListener instance;
	public static TableCreateEventListener getInstance(){
		if(instance==null){
			instance = new TableCreateEventListener();
		}
		
		return instance;
	}
	
	@Override
	public void registerEvent() {
		// TODO Auto-generated method stub
		EventEngine.registerLogicEvent(LogicEventType.CREATETABLE, this);		
	}
	@Override
	public void fireEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToQueue(Event event) {
		// TODO Auto-generated method stub
		
	}

}
