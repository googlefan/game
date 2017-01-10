package com.zipeiyi.game.common.code;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.zipeiyi.game.common.message.MessageReq;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class Decoder extends ByteToMessageDecoder{
    private static Codec<MessageReq> messageCode;

    static {
        try {
            messageCode = ProtobufProxy.create(MessageReq.class);
        }catch (Exception e){
            System.out.println(e);
        }
    }


    @Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
        try{
            byte[] bytes = new byte[in.readableBytes()] ;

            in.readBytes(bytes);
            MessageReq recMsg = messageCode.decode(bytes);
            out.add(recMsg);
        }catch (Exception e){
            System.out.println(e);
        }

	}

}
