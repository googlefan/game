package com.common.nio.socket;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;

import io.netty.channel.ChannelHandlerContext;

public class Session {

	 String clientId;
	 ChannelHandlerContext ctx;

	 String serverID;
	 String clientHost;
	 Integer clientPort;

	public Session() {

	}

	public Session(String clientId, ChannelHandlerContext ctx) {

		this.clientId = clientId;
		this.ctx = ctx;
	}

	public void write(MessageRes response) {

		if (isActive()) {
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&7"+response);
			ctx.writeAndFlush(response);
		}
	}

	public void send(MessageReq req) {
		if (isActive()) {
//			System.out.println("send message=======111::"+req);
			ctx.writeAndFlush(req);
		}
	}

	public boolean isActive() {
		return ctx.channel().isActive();
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}

	public Integer getClientPort() {
		return clientPort;
	}

	public void setClientPort(Integer clientPort) {
		this.clientPort = clientPort;
	}

	public String getServerID() {
		return serverID;
	}

	public void setServerID(String serverID) {
		this.serverID = serverID;
	}

}
