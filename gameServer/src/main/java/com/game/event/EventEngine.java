package com.game.event;

import java.util.List;
import java.util.Map;

import com.game.listener.TableCreateEventListener;
import com.game.constant.EventType;
import com.game.constant.LogicEventType;
import com.game.listener.ILogicEventListener;
import com.game.listener.ITableEnventListener;
import com.game.listener.TableJobEventListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EventEngine {

	private static EventEngine instance = new EventEngine();
	
	private static List<ILogicEventListener> listeners = Lists.newArrayList();

	private static Map<LogicEventType, List<ILogicEventListener>> eventMap = Maps.newHashMap();

	public static EventEngine getInstance(){
		return instance;
	}
	public static void start() {
		listeners.add(TableCreateEventListener.getInstance());
		listeners.add(TableJobEventListener.getInstance());

		for (ILogicEventListener listener : listeners) {
			listener.registerEvent();
		}
	}

	/**
	 * 注册事件
	 * 
	 * @param event
	 * @param listener
	 */
	public static void registerLogicEvent(LogicEventType event, ILogicEventListener listener) {

		List<ILogicEventListener> list = eventMap.get(event);
		if (list == null) {
			list = Lists.newArrayList();
			eventMap.put(event, list);
		}
		list.add(listener);
	}

	/**
	 * 添加事件到队列
	 * 
	 * @param event
	 */
	public static void addToQueue(Event event) {
		// TODO Auto-generated method stub
		EventType type = event.getEventType();

		if (type instanceof LogicEventType) {
			LogicEventType _type = event.getLogicEvent();
			List<ILogicEventListener> list = eventMap.get(_type);
			if (list == null) {
				return;
			}

			for (ILogicEventListener listener : list) {
				if(listener instanceof ITableEnventListener){
					((ITableEnventListener) listener).addToQueue(event);
				}
			}
		}
	}

	/**
	 * 触发事件
	 * 
	 * @param event
	 */
	public void event(Event event) {
		EventType type = event.getEventType();

		if (type instanceof LogicEventType) {
			LogicEventType _type = event.getLogicEvent();
			List<ILogicEventListener> list = eventMap.get(_type);
			if (list == null) {
				return;
			}

			for (ILogicEventListener listener : list) {
				listener.fireEvent(event);
			}
		}
	}
}
