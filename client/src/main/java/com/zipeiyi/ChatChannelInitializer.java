package com.zipeiyi;

import com.zipeiyi.game.common.code.ClientDecoder;
import com.zipeiyi.game.common.code.ClientEncoder;
import com.zipeiyi.game.common.code.Decoder;
import com.zipeiyi.game.common.code.Encoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by zhuhui on 16-12-5.
 */
public class ChatChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
//        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//        pipeline.addLast("decoder", new StringDecoder());
//        pipeline.addLast("encoder", new StringEncoder());
//        pipeline.addLast("handler", new ChatClientHandler());
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast("protobufDecoder", new ClientDecoder());
        pipeline.addLast("protobufEncoder", new ClientEncoder());
        pipeline.addLast("handler", new ChatClientHandler());
    }
}
