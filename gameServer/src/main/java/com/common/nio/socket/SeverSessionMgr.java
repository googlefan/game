package com.common.nio.socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.nio.processor.Processor;

import io.netty.channel.ChannelHandlerContext;

public class SeverSessionMgr {

	private static final Logger logger = LoggerFactory.getLogger(SeverSessionMgr.class);
	private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

	private static volatile SeverSessionMgr instance = null;

	public static SeverSessionMgr getInstance() {

		if (instance == null) {
			synchronized (SeverSessionMgr.class) {
				instance = new SeverSessionMgr();

				return instance;
			}
		}
		return instance;
	}

	public void onInActive() {

	}

	public void onActive() {

	}

	public void setProcessors(Map<Integer, Processor> processors) {

	}

	public void createSession(String clientId, ChannelHandlerContext ctx) {
		logger.info("新增客户端session clientId:::" + clientId);
		Session session = new Session(clientId, ctx);
		sessionMap.put(clientId, session);
	}

	public Session getSesion(String clientId) {
		logger.info("获取客户端session clientId:::" + clientId);
		Session session = sessionMap.get(clientId);

		return session;
	}

	public void removeSession(String clientId, ChannelHandlerContext ctx) {
		sessionMap.remove(clientId, ctx);
	}

}
