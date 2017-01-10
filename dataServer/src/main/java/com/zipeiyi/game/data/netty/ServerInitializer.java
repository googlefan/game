package com.zipeiyi.game.data.netty;

import com.zipeiyi.game.common.code.ServerDecoder;
import com.zipeiyi.game.common.code.ServerEncoder;
import com.zipeiyi.game.data.dispatcher.HandlerDispatcher;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private HandlerDispatcher handlerDispatcher;

    public void init() {
        new Thread(this.handlerDispatcher).start();
    }

    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast("protobufDecoder", new ServerDecoder());
        pipeline.addLast("protobufEncoder", new ServerEncoder());
        pipeline.addLast("handler", new ServerAdapter(this.handlerDispatcher));
    }

    public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
        this.handlerDispatcher = handlerDispatcher;
    }

}