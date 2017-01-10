package com.zipeiyi.game.gateServer.netty;

import com.zipeiyi.game.common.code.*;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.gateServer.handle.ClientHandlerDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by zhangxiaoqiang on 16/12/2.
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private ClientHandlerDispatcher clientHandlerDispatcher = new ClientHandlerDispatcher();

    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        ch.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        ch.pipeline().addLast("protobufDecoder",new ClientDecoder());
        ch.pipeline().addLast("protobufEncoder",new ClientEncoder());
        pipeline.addLast("handler", clientHandlerDispatcher);
    }

    public MessageRes getResponse(final long messageId) throws Exception {return clientHandlerDispatcher.getResponse(messageId);}

    public void sendRequest(Channel channel,final Object msg){
        clientHandlerDispatcher.write(channel,msg);
    }

}
