package com.zipeiyi.game.gateServer.netty;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.gateServer.domain.KeepAliveMessage;
import com.zipeiyi.game.gateServer.handle.HandlerDispatcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerAdapter extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ServerAdapter.class);
	
	private HandlerDispatcher handlerDispatcher;

	public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

	public ServerAdapter(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

	public ServerAdapter() {
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageReq message = (MessageReq)msg;
		handlerDispatcher.addMessage(message);
	}


	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
	}
	
	

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("ServerAdapter handler error,msg：" + cause.getMessage());
        KeepAliveMessage.removeKeepAliveInfo(ctx.channel());
//        ctx.channel().close();
	}

}
