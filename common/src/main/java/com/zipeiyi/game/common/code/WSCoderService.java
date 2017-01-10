package com.zipeiyi.game.common.code;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.util.JsonUtil;
import com.zipeiyi.game.common.util.TranscodUtiling;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSCoderService {
	private static final Logger logger = LoggerFactory.getLogger(WSCoderService.class);
	private static final String WEBSOCKET_PATH = "/";

	WebSocketServerHandshaker handshaker;
	// 在线用户channel map,由ip：channel组成
	// private static final Map<String, Channel> onlineUserChannelMap = new
	// HashMap<String, Channel>();

	/**
	 * 
	 * @function: 处理websocket请求业务
	 * @param ctx
	 * @param frame
	 * @throws IOException
	 */
	public MessageReq handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws IOException {

		// 是否关闭
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return null;
		}
		// 是否ping
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return null;
		}
		// 文本
		if ((frame instanceof TextWebSocketFrame)) {
			// 接收到的消息
			String s = ((TextWebSocketFrame) frame).text();
			// 发送者的通道
			Channel incoming = ctx.channel();
			logger.info("TextWebSocketFrame收到客户[" + incoming.remoteAddress() + "]消息：" + s);
			// 从发送消息解析出接收人的用户id
			// String acceptUserId = null;
			// 解析出接收者的用户id
			// if (s != null && s.contains("||-||")) {
			// acceptUserId = s.split("||-||")[1];
			// }
			// 点对点发送消息
			// if (acceptUserId != null) {
			// Channel channel = onlineUserChannelMap.get(acceptUserId);
			// if (channel != null) {
			// channel.writeAndFlush(new TextWebSocketFrame("[" +
			// channel.remoteAddress() + "]:" + s + "\n"));
			// }
			// }

		} else {
			ByteBuf buffer = frame.content();
			int s = buffer.readableBytes();
			byte[] data = new byte[s];
			buffer.readBytes(data);
			data = Arrays.copyOfRange(data, 4, s);
			MessageReq req = ServerDecoder.getReqMessage(data);
			return req;
		}
		return null;
	}

	/**
	 * @function: http请求处理
	 * @param ctx
	 * @param req
	 * @throws Exception
	 */
	public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		// http get请求
		if (req.getMethod() == HttpMethod.GET) {
			httpGetDeal(ctx, req);
		}
		if (req.getMethod() == HttpMethod.POST) {
			httpPostDeal(ctx, req);
			return;
		}
	}

	/**
	 * @function: http post方式处理
	 * @param ctx
	 * @param req
	 * @throws IOException
	 */
	private void httpPostDeal(ChannelHandlerContext ctx, FullHttpRequest req) throws IOException {
		// 处理POST请求
		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
		InterfaceHttpData postData = decoder.getBodyHttpData("q"); // //
																	// 读取从客户端传过来的参数
		String question = "";
		if (postData.getHttpDataType() == HttpDataType.Attribute) {
			Attribute attribute = (Attribute) postData;
			question = attribute.getValue();
			System.out.println("q:" + question);

		}
		if (question != null && !question.equals("")) {
			String data = question;
			FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
					Unpooled.wrappedBuffer(data.getBytes(CharsetUtil.UTF_8)));

			res.headers().set(Names.CONTENT_TYPE, "text/html; charset=UTF-8");
			res.headers().set(Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			res.headers().set(Names.CONTENT_LENGTH, res.content().readableBytes());
			sendHttpResponse(ctx, req, res);

		}
		return;
	}

	/**
	 * 
	 * @function: 获得本地webscoket地址
	 * @param req：http请求对象
	 * @return String ：返回地址字符串
	 */
	private static String getWebSocketLocation(FullHttpRequest req) {
		String location = req.headers().get(Names.HOST) + WEBSOCKET_PATH;

		return "ws://" + location;

	}

	/**
	 * @function: http get方式处理
	 * @param ctx
	 * @param req
	 *            void
	 */
	private void httpGetDeal(ChannelHandlerContext ctx, FullHttpRequest req) {
		// 如果get请求地址中包括websocket

		if (req.getUri().equals("/")) {
			logger.info("update http request upgrade to websocket,create handshaker...");
			// Handshake
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req),
					null, false);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
			}
		} else
		// 处理get请求
		{
			QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
			Map<String, List<String>> parame = decoder.parameters();
			// 读取从客户端传过来的参数
			List<String> q = parame.get("q");
			// 参数值
			String question = q.get(0);
			// 根据参数值处理对应的业务
			if (question != null && question.equals("getData")) {
				System.out.println("r :" + question);
				String data = question;
				// 返回内容
				FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(data.getBytes(CharsetUtil.UTF_8)));

				res.headers().set(Names.CONTENT_TYPE, "text/html; charset=UTF-8");
				res.headers().set(Names.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
				res.headers().set(Names.CONTENT_LENGTH, res.content().readableBytes());
				sendHttpResponse(ctx, req, res);
			}

		}
	}

	/**
	 * 
	 * @function: http请求响应处理
	 * @param ctx
	 * @param req
	 * @param res
	 *            void
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			HttpHeaders.setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	
	public static void senMessageToClient(Channel channle, MessageRes res) throws Exception {
		logger.info("send message to client before server encode,message response:{}", JsonUtil.getJsonFromBean(res));
		byte[] data = ServerEncoder.getCommondMes(res);
		logger.info("返回客户端字节长度:{},字节数据:{} ",data.length,Arrays.toString(data));
		ByteBuf buf = Unpooled.copiedBuffer(TranscodUtiling.intToBytes(data.length), data);
		ChannelFuture future = channle.writeAndFlush(new BinaryWebSocketFrame(buf));
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				if (future.isSuccess()) {
					logger.info(future.channel() + ",成功发送数据到客户端");
				} else {
					Channel channel = future.channel();
					Throwable cause = future.cause();
					logger.error("当前channel[{"+channel+"}]发送数据包失败.",cause);
				}
			}
		});
	}

}
