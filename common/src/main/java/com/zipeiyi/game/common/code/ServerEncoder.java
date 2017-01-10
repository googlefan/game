package com.zipeiyi.game.common.code;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.zipeiyi.game.common.message.Common;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.util.JsonUtil;
import com.zipeiyi.game.common.util.TranscodUtiling;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerEncoder extends MessageToByteEncoder<MessageRes> {

    private static final Logger logger = LoggerFactory.getLogger(ServerEncoder.class);

    private static Codec<Common> messageCode = ProtobufProxy.create(Common.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageRes msg, ByteBuf out) throws Exception {
        // TODO Auto-generated method stub

        try {
            byte[] data = getCommondMes(msg);// messageCode.encode(msg);
            ByteBuf buf = Unpooled.copiedBuffer(TranscodUtiling.intToBytes(data.length), data);// 在写消息之前需要把消息的长度添加到投4个字节
            out.writeBytes(buf);
        } catch (Exception e) {
            logger.error("serverEncoder Error===============" + e);
        }

    }

    public static byte[] getCommondMes(MessageRes response) throws Exception {
        StringBuffer strb = new StringBuffer();
        Common common = new Common();
        common.setUid(response.getUid());
        common.setCode(response.getCode());
        common.setMsg(response.getMsg());
        common.setCmd(response.getCmd());
        common.setModuleId(response.getModuleId());
        common.setSeque(response.getSeque());

        byte[] b = null;
        if (response.getObj() != null) {
            logger.debug("ServerEncoder get object class********************" + response.getObj().getClass());
            b = TranscodUtiling.encode(response.getObj(), response.getObj().getClass());
        }
        common.setObj(b);
        strb.append("ServerEncoder, res message:" + JsonUtil.getJsonFromBean(common));

        logger.info(strb.toString());
        return messageCode.encode(common);
    }

}
