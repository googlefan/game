package com.game.listener;

import com.game.constant.EventType;
import com.game.event.Event;

public class LogicEventListener implements ILogicEventListener{

	private static LogicEventListener listener;
	
	public static LogicEventListener getInstance(){
		if(listener == null) {
			listener = new LogicEventListener();
		}
		
		return listener;
	}

	@Override
	public void registerEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireEvent(Event event) {
		// TODO Auto-generated method stub
		
	}
	
	
}
