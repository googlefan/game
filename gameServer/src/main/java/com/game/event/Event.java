package com.game.event;

import com.game.constant.EventType;
import com.game.constant.LogicEventType;

public abstract class Event { 	 	

	private EventType eventType;  
	private LogicEventType logicEvent;

	public LogicEventType getLogicEvent() {
		return logicEvent;
	}

	public void setLogicEvent(LogicEventType logicEvent) {
		this.logicEvent = logicEvent;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}	
	
}
