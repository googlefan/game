package com.zipeiyi.game.gateServer.netty;


import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.service.ProtoClassLoadService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangxiaoqiang on 16/12/1.
 */
public class NettyClient implements Client{

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup workerGroup;
    private Channel channel;
    private int workerGroupThreads = 1000;

    private ClientInitializer clientInitializer = new ClientInitializer();


    public NettyClient(String ip,int port) throws Exception {
        this.connect(ip,port);
    }

    public NettyClient(String ip,int port,int workerGroupThreads) throws Exception {
        this.workerGroupThreads = workerGroupThreads;
        this.connect(ip,port);
    }

    private void connect(String ip,int port) throws Exception{
        workerGroup = new NioEventLoopGroup(workerGroupThreads);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(clientInitializer);
        ChannelFuture future = bootstrap.connect(ip,port).sync();
        channel = future.channel();
        logger.info("======gate client is started======");
//        channel.closeFuture().syncUninterruptibly();
//        future.channel().closeFuture().sync();
//        channel = bootstrap.connect(ip,port).syncUninterruptibly().channel();
    }

    public boolean checkAlive(){
        return channel.isActive();
    }

    @Override
    public MessageRes sent(MessageReq request) throws Exception{
        clientInitializer.sendRequest(channel,request);
        return clientInitializer.getResponse(request.getSeque());
    }

    @Override
    public void close() {
        if (null == channel) {
            logger.error("客户端关闭异常。。。");
            return;
        }
        workerGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        workerGroup = null;
        channel = null;
    }
}
