package com.common.nio.handler;

import java.net.InetSocketAddress;

import com.common.nio.processor.Dispatcher;
import com.common.nio.socket.Session;
import com.common.nio.socket.SeverSessionMgr;
import com.zipeiyi.game.common.message.MessageReq;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ChannelServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
			MessageReq request = (MessageReq) msg;

			Session session = SeverSessionMgr.getInstance().getSesion(ctx.channel().id().asLongText());
			Dispatcher.getInstance().dispatcher(session, request);

			System.out.println("接收客户端time:[" + request.getTime() + "]");
//		}
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		try {
			String channelId = ctx.channel().id().asLongText();
			InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
			String adre = address.getAddress().getHostAddress();

			SeverSessionMgr.getInstance().createSession(channelId, ctx);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		String adre = address.getAddress().getHostAddress();
		System.out.println("receive client disconnect..........." + adre);
	}

	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		Channel channel = ctx.channel();
		System.out.println("Client[" + channel.remoteAddress() + "]异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}

}
