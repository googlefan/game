package com.zipeiyi.game.common.code;

import java.io.IOException;
import java.util.List;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.zipeiyi.game.common.message.Common;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.util.JsonUtil;
import com.zipeiyi.game.common.util.TranscodUtiling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(ServerDecoder.class);
	private static Codec<Common> messageCode;

	public ServerDecoder() {}
	static{
		messageCode = ProtobufProxy.create(Common.class);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		try {
			byte[] bytes = new byte[in.readableBytes()];

			in.readBytes(bytes);

			// Common common = messageCode.decode(bytes);
			// byte[] data = common.getObj();
			// int cmd = common.getCmd();
			//
			// Object obj = null;
			// if(data!=null){
			// Class clazz = CmdClassManager.getReq_obj_class().get(cmd);
			// obj = TranscodUtiling.decode(data,clazz);// clazz.newInstance();
			// }
			//

			MessageReq request = getReqMessage(bytes);
			// request.setCmd(common.getCmd());
			// request.setUid(common.getUid());
			// request.setSeque(common.getSeque());
			// request.setModuleId(common.getModuleId());
			// request.setChannelCode(common.getChannelCode());
			// request.setObj(obj);

			out.add(request);
		} catch (Exception e) {
			logger.error("ServerDecode====Error::" + e + " ctx:" + ctx + " , in:" + in);
		}
	}

	public static MessageReq getReqMessage(byte[] bytes) throws IOException {

		Common common = messageCode.decode(bytes);
		byte[] data = common.getObj();
		int cmd = common.getCmd();

		Object obj = null;
		if (data != null && data.length > 0) {
			Class clazz = CmdClassManager.getReq_obj_class().get(cmd);
			logger.debug("ServerDecoder decoding obj====cmd:" + cmd + " ClassName+:" + clazz.getSimpleName());

			obj = TranscodUtiling.decode(data, clazz);// clazz.newInstance();
		}

		MessageReq request = new MessageReq();
		request.setTime(common.getTime());
		request.setToken(common.getToken());
		request.setCmd(common.getCmd());
		request.setUid(common.getUid());
		request.setSeque(common.getSeque());
		request.setModuleId(common.getModuleId());
		request.setChannelCode(common.getChannelCode());
		request.setObj(obj);
		logger.info("ServerDecoder,req message:{}", JsonUtil.getJsonFromBean(request));
		return request;
	}
}
