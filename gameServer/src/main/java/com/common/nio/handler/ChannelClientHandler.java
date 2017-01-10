package com.common.nio.handler;

import com.common.nio.processor.DBClientDispatcher;
import com.common.nio.socket.ClientSessionMgr;
import com.common.nio.socket.Session;
import com.zipeiyi.game.common.message.MessageRes;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ChannelClientHandler extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		
		MessageRes  response= (MessageRes)msg;
        String channelId = ctx.channel().id().asLongText();
        Session session = ClientSessionMgr.getInstance().getSession(channelId);
        
        DBClientDispatcher.getInstance().onDispatcher(session, response);
		System.out.println("client Receive Msg:::"+response.getMsg());
		
	}


	@Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
                
        String channelId = ctx.channel().id().asShortText();
        ClientSessionMgr.getInstance().createClientSession("dbServer", ctx);
//        System.out.println("connect success=============="+channelId);
        
    }  
	
	
}
