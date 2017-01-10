package com.zipeiyi.game.common.code;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.zipeiyi.game.common.message.Common;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.util.TranscodUtiling;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 * 考虑gate中间数据，不需要解析，故object不解析特殊处理
 */
public class GateClientDecoder extends ByteToMessageDecoder {

    private static Codec<Common> messageCode = ProtobufProxy.create(Common.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // TODO Auto-generated method stub
        byte[] bytes = new byte[in.readableBytes()] ;

        in.readBytes(bytes);

        Common common = messageCode.decode(bytes);
        byte[] data = common.getObj();

        MessageRes response = new MessageRes();
        response.setCode(response.getCode());
        response.setMsg(response.getMsg());
        response.setCmd(common.getCmd());
        response.setSeque(common.getSeque());
        response.setModuleId(common.getModuleId());
        response.setObj(data);

        // MessageReq recMsg =
        // byte[] objByte = recMsg.getObj();
        // messageCode.decode(objByte);

        out.add(response);
    }

}
