package com.common.nio.socket;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

public class ClientSessionMgr{
	
	
	private Map<String,Session> clientSessionMap = new HashMap<>();
	private static volatile ClientSessionMgr instance;
	
	public static ClientSessionMgr getInstance(){
		
		if(instance == null){
			instance = new ClientSessionMgr();
		}
		return instance;
	}
	
	public void createClientSession(String channelId,ChannelHandlerContext ctx){
		Session session = new DBClientSession(channelId, ctx);
		
		this.clientSessionMap.put(channelId, session);
	}
	
	public void removeSession(String channelId,ChannelHandlerContext ctx){
		
		this.clientSessionMap.remove(channelId, ctx);
	}
	
	
	public Session getSession(String channelId){
		return this.clientSessionMap.get(channelId);
	}
	
}
