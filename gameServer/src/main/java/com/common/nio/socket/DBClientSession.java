package com.common.nio.socket;

import com.zipeiyi.game.common.message.MessageReq;

import io.netty.channel.ChannelHandlerContext;

public class DBClientSession extends Session{

	public DBClientSession(String serverID, ChannelHandlerContext ctx) {
		// super(clientId, ctx);
		this.serverID = serverID;
		this.ctx = ctx;
		// TODO Auto-generated constructor stub
	}
	
	public void write(MessageReq request){
		
		if(isActive()){
			getCtx().writeAndFlush(request);
		}
	}
}
