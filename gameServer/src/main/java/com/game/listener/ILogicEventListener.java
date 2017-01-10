package com.game.listener;

import com.game.event.Event;

public interface ILogicEventListener {

	public void registerEvent();
	
	public void fireEvent(Event event);
	
}
