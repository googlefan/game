package com.zipeiyi.game.gateServer.handle;

import com.zipeiyi.game.common.code.WSCoderService;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.gateServer.domain.KeepAliveMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/1/6.
 */
public class HandshakeHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(HandshakeHandler.class);
    private WSCoderService wsCoderService;

    public HandshakeHandler(WSCoderService wsCoderService){
        this.wsCoderService = wsCoderService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageReq message = null;
        if (msg instanceof FullHttpRequest) {
            wsCoderService.handleHttpRequest(ctx, (FullHttpRequest) msg);
        }  else if (msg instanceof WebSocketFrame){
            message = wsCoderService.handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            if(message != null){
                //设置全局channel与uid对应关系
                KeepAliveMessage.KeepAliveInfo keepAliveInfo = new KeepAliveMessage.KeepAliveInfo();
                keepAliveInfo.gateChannel = ctx.channel();
                keepAliveInfo.uid = message.getUid();
                KeepAliveMessage.addKeepAlive(keepAliveInfo);
                message.setChannelCode(keepAliveInfo.gateChannel.hashCode());
                ctx.fireChannelRead(message);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("LoginCheck handler error,msg："+cause.getMessage());
//        KeepAliveMessage.removeKeepAliveInfo(ctx.channel());
//        ctx.channel().close();
    }
}
