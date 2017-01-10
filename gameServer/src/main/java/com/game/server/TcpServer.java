package com.game.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.log4j.Log4jGameConfigurer;
import com.common.nio.handler.ChannelServerHandler;
import com.common.nio.processor.Dispatcher;
import com.common.nio.processor.ProcessorLoader;
import com.common.nio.socket.TcpDBClient;
import com.game.db.redis.RedisDao;
import com.game.db.redis.impl.RedisDaoImp;
import com.game.event.EventEngine;
import com.game.quartz.GameQuartzTaskService;
import com.game.quartz.QuartzService;
import com.zipeiyi.game.common.code.ServerDecoder;
import com.zipeiyi.game.common.code.ServerEncoder;
import com.zipeiyi.game.common.service.ProtoClassLoadService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class TcpServer {

	private static final int PORT = 9001;
	private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);
	// private GameScheduleService scheduleService = new GameScheduleService();
	private Dispatcher dispatcher = Dispatcher.getInstance();
	// private GameConfigDataLoadService gameConfigService =
	// GameConfigDataLoadService.getInstance();

	public void start() {
		// ApplicationContext context = new ApplicationContext();
		Log4jGameConfigurer.initLogging();

		EventLoopGroup bossGroup = new NioEventLoopGroup(3);
		EventLoopGroup workGroup = new NioEventLoopGroup(10);

		try {
			ServerBootstrap bootServer = new ServerBootstrap();
			bootServer.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.SO_KEEPALIVE, true)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ChannelPipeline piple = ch.pipeline();
							// piple.addLast("http-codec", new
							// HttpServerCodec());
							// piple.addLast("aggregator", new
							// HttpObjectAggregator(65536)); // 定义缓冲大小
							// piple.addLast("http-chunked", new
							// ChunkedWriteHandler());
							piple.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
							piple.addLast("decoder", new ServerDecoder());
							piple.addLast("encoder", new ServerEncoder());
							piple.addLast(new ChannelServerHandler()); // 设置处理handler
						}

					});

			// 加载processor
			ProcessorLoader processorLoader = new ProcessorLoader(dispatcher);
			processorLoader.load();

			// //加载配置数据
			// gameConfigService.registerConfLoader();

			// 启动事件监听
			EventEngine.start();
			ProtoClassLoadService.getInstance().Init();
			// //任务线程
			// scheduleService.registerSchedule();
			//
			QuartzService tz = QuartzService.getInstance();
			tz.start();
			new GameQuartzTaskService(tz);

			ChannelFuture f = bootServer.bind(PORT).sync();
			if (f.isSuccess()) {
				logger.info("start netty server success");
				System.out.println("start netty server success");
			}

			Thread.sleep(3000);
			
			Map<String, Double> map = RedisDaoImp.getInstance().getNoFullTableIds();
			logger.info("删除table id:::"+map);
			for (String keys : map.keySet()) {
				logger.info("删除table id:::"+keys);
				RedisDaoImp.getInstance().delTableVo(keys);;
			}
			RedisDaoImp.getInstance().getAllTableVos();
			
			// 连接DBServer
			TcpDBClient dbClient = new TcpDBClient();
			dbClient.start();
			
			
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.info("start error!!!!!!!1");
			e.printStackTrace();
		}

		finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	public static void main(String args[]) {

		TcpServer server = new TcpServer();
		server.start();
	}

}
