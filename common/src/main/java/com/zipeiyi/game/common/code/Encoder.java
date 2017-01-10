package com.zipeiyi.game.common.code;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.zipeiyi.game.common.message.MessageRes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<MessageRes> {

    private static Codec<MessageRes> messageCode;

    static {
        messageCode = ProtobufProxy.create(MessageRes.class);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageRes msg, ByteBuf out) throws Exception {
        // TODO Auto-generated method stub

        try {
            byte[] data = messageCode.encode(msg);
            ByteBuf buf = Unpooled.copiedBuffer(intToBytes(data.length), data);//在写消息之前需要把消息的长度添加到投4个字节
            System.out.println("encoding====");
            out.writeBytes(buf);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static int bytesToInt(byte[] data, int offset) {
        int num = 0;
        for (int i = offset; i < offset + 4; i++) {
            num <<= 8;
            num |= (data[i] & 0xff);
        }
        return num;
    }

    /**
     * 将整形转化成字节
     *
     * @param num
     * @return
     */
    public static byte[] intToBytes(int num) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }

}
