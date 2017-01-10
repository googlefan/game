package com.zipeiyi.game.gateServer.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zipeiyi.game.common.code.CmdClassManager;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.proto.DBHallUserInfoReq;
import com.zipeiyi.game.common.service.ProtoClassLoadService;

import java.net.InetSocketAddress;

public class NettyServer {
	private Logger logger = LoggerFactory.getLogger(getClass());
	/** 用于分配处理业务线程的线程组个数 */
	protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2; 
	/** 业务出现线程大小 */
	protected static final int BIZTHREADSIZE = 4;

	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

	private ServerInitializer initializer;
	private final int port;

	public NettyServer(int port) {
		this.port = port;
	}

	public void setInitializer(ServerInitializer initializer) {
		this.initializer = initializer;
	}

	public void run() throws Exception {

		try {
			ServerBootstrap b = new ServerBootstrap();
			((ServerBootstrap) b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class))
                    .option(ChannelOption.SO_BACKLOG,3096)//最大客户端连接数
//                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
					.childHandler(this.initializer);

			Channel ch = b.bind(new InetSocketAddress(this.port)).sync().channel();
            logger.info("======server is started======");
            ProtoClassLoadService.getInstance().Init();

 
			ch.closeFuture().syncUninterruptibly();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
