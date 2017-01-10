package com.zipeiyi.game.common.code;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.zipeiyi.game.common.message.Common;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.util.JsonUtil;
import com.zipeiyi.game.common.util.TranscodUtiling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(ClientDecoder.class);
	private static Codec<Common> messageCode = ProtobufProxy.create(Common.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			byte[] bytes = new byte[in.readableBytes()];
			in.readBytes(bytes);
			Common common = messageCode.decode(bytes);
			byte[] data = common.getObj();
			int cmd = common.getCmd();

			Object obj = null;
			if (data != null) {
				Class clazz = CmdClassManager.getRes_obj_class().get(cmd);
				obj = TranscodUtiling.decode(data, clazz);// clazz.newInstance();
			}

			MessageRes response = new MessageRes();
			response.setUid(common.getUid());
			response.setCode(common.getCode());
			response.setMsg(common.getMsg());
			response.setCmd(common.getCmd());
			response.setSeque(common.getSeque());
			response.setModuleId(common.getModuleId());
			response.setUidList(common.getUidList());
			response.setObj(obj);
			logger.info("ClientDecoder,req message:{}", JsonUtil.getJsonFromBean(response));
			out.add(response);
		} catch (Exception e) {
			System.out.println("ClientDecoder Error=======" + e);
		}

	}

}
