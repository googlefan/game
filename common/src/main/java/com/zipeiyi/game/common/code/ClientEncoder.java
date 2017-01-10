package com.zipeiyi.game.common.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.zipeiyi.game.common.message.Common;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.util.JsonUtil;
import com.zipeiyi.game.common.util.TranscodUtiling;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ClientEncoder extends MessageToByteEncoder<MessageReq> {
	private static final Logger logger = LoggerFactory.getLogger(ClientEncoder.class);
	static Codec<Common> messageCode = ProtobufProxy.create(Common.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageReq msg, ByteBuf out) throws Exception {
		try {
			byte[] data = getCommondMes(msg);
			ByteBuf buf = Unpooled.copiedBuffer(TranscodUtiling.intToBytes(data.length), data);// 在写消息之前需要把消息的长度添加到投4个字节
			out.writeBytes(buf);
		} catch (Exception e) {
			System.out.println("ClientEncoder Error=====" + e);
		}

	}

	public static byte[] getCommondMes(MessageReq request) throws Exception {

		StringBuffer strb = new StringBuffer();
		Common common = new Common();

		common.setCmd(request.getCmd());
		common.setModuleId(request.getModuleId());
		common.setSeque(request.getSeque());
		common.setUid(request.getUid());
		common.setTime(request.getTime());
		common.setToken(request.getToken());
		// common.set

		byte[] b = null;
		if (request.getObj() != null) {
			b = TranscodUtiling.encode(request.getObj(), request.getObj().getClass());
		}
		common.setObj(b);
		strb.append("ClientEncoder,req message:" + JsonUtil.getJsonFromBean(common));
		logger.info(strb.toString());
		return messageCode.encode(common);

	}

}
