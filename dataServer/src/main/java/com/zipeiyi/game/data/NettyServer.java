package com.zipeiyi.game.data;

import com.zipeiyi.game.common.service.ProtoClassLoadService;
import com.zipeiyi.game.data.netty.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyServer {
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 用于分配处理业务线程的线程组个数
     */
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;
    /**
     * 业务出现线程大小
     */
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
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.initializer);

            this.logger.info(" server started at port " + this.port + '.');
            ProtoClassLoadService.getInstance().Init();
            Channel ch = b.bind(new InetSocketAddress(this.port)).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
