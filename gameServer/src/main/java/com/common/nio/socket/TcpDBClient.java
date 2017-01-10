package com.common.nio.socket;

import com.common.nio.handler.ChannelClientHandler;
import com.game.db.dao.CenterDBDao;
import com.game.service.DBServerConfigLoad;
import com.game.service.GameServerConfigLoad;
import com.game.service.GameServerConfigLoad.GameServer;
import com.zipeiyi.game.common.code.ClientDecoder;
import com.zipeiyi.game.common.code.ClientEncoder;
import com.zipeiyi.game.common.code.CommonCodeFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class TcpDBClient {
	String host = "192.169.20.119";
	int port = 8000;
	DBServerConfigLoad.DBServer dbServer = DBServerConfigLoad.get().dbServerList.get(0);
	//
	// String host = "127.0.0.1";
	// int port = 9001;

	public void start() throws InterruptedException {

		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

		try {

			Bootstrap bootstrap = new Bootstrap();
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.group(eventLoopGroup);
			bootstrap.remoteAddress(host, port);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					ChannelPipeline piple = socketChannel.pipeline();
					piple.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
					piple.addLast("decoder", new ClientDecoder());
					piple.addLast("encoder", new ClientEncoder());

					// new ChannelClientHandler();
					piple.addLast("handler", new ChannelClientHandler());
				}
			});
			ChannelFuture future = bootstrap.connect(dbServer.ip, dbServer.port).sync();
			if (future.isSuccess()) {
				// SocketChannel socketChannel = (SocketChannel)
				// future.channel();
				// socketChannel
				System.out.println("----------------connect server success----------------");

//				Thread.sleep(3000);
//				CenterDBDao.getInstance().getCardsList("table1", 10);
			}
			future.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {

		TcpDBClient client = new TcpDBClient();
		client.start();

	}

}
