package com.game.listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.bjf.remoting.protobuf.utils.StringUtils;
import com.game.constant.LogicEventType;
import com.game.event.Event;
import com.game.event.EventEngine;
import com.game.event.GameLogicEvent;
import com.game.module.TexasHoldemServerImp;
import com.game.vo.TableVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TableJobEventListener extends ITableEnventListener implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(TableJobEventListener.class);

	static ExecutorService threadPool = Executors.newFixedThreadPool(10);
	private static ConcurrentMap<String, List<GameLogicEvent>> tableQueue = Maps.newConcurrentMap();

	private static TableJobEventListener instance;

	public static TableJobEventListener getInstance() {
		if (instance == null) {
			instance = new TableJobEventListener();
		}

		return instance;
	}

	public TableJobEventListener() {
		// ReflectRunnable task = new ReflectRunnable();
		Thread th = new Thread(this);
		th.start();
	}

	public void addToQueue(Event event) {
		GameLogicEvent _event = (GameLogicEvent) event;

		List<GameLogicEvent> list = tableQueue.get(_event.getTableID());
		if (list == null) {
			list = Lists.newArrayList();
			tableQueue.put(_event.getTableID(), list);
		}
		list.add(_event);

	}

	public void removeJob(String tableID, TableVo tableVo) {

		if (StringUtils.isEmpty(tableID) || tableVo == null) {

		}
	}

	public void doJob() throws NoSuchMethodException, SecurityException {

		for (String tableID : tableQueue.keySet()) {
			List<GameLogicEvent> list = tableQueue.get(tableID);
			if (!list.isEmpty()) {
				Iterator<GameLogicEvent> iterator = list.iterator();

				while (iterator.hasNext()) {

					GameLogicEvent event = iterator.next();
					Method method = null;
					switch (event.getLogicEvent()) {
					case CHOOSE_CARDS:
						method = getGameService().getClass().getMethod("chooseCard", new Class[] { event.getClass() });
						// getGameService().chooseCard(event);
						break;
					case GRAB_BANKER:
						method = getGameService().getClass().getMethod("grabBanker", new Class[] { event.getClass() });
						// getGameService().grabBanker(event);
						break;
					case RAISE:
						method = getGameService().getClass().getMethod("raise", new Class[] { event.getClass() });
						// getGameService().raise(event);
						break;
					default:
						logger.warn("");
						break;

					}
					ReflectRunnable ru = new ReflectRunnable(getGameService(), method);
					threadPool.submit(ru);
					iterator.remove();
				}
			}

		}
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				doJob();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void registerEvent() {
		// TODO Auto-generated method stub
		EventEngine.registerLogicEvent(LogicEventType.CHOOSE_CARDS, this);
		EventEngine.registerLogicEvent(LogicEventType.GRAB_BANKER, this);
		EventEngine.registerLogicEvent(LogicEventType.RAISE, this);

	}

	@Override
	public void fireEvent(Event event) {
		// TODO Auto-generated method stub
		addToQueue(event);
	}
}

class ReflectRunnable implements Runnable {

	TexasHoldemServerImp gameService;
	Method currentMethod;
	Object params;

	public ReflectRunnable(TexasHoldemServerImp service, Method method) {
		this.gameService = service;
		this.currentMethod = method;
	}

	public ReflectRunnable(TexasHoldemServerImp service, Method method, Object... params) {
		this.gameService = service;
		this.params = params;
		this.currentMethod = method;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			currentMethod.invoke(gameService);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
