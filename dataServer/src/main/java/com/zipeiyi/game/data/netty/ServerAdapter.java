package com.zipeiyi.game.data.netty;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.data.dispatcher.HandlerDispatcher;
import com.zipeiyi.game.data.domain.ChannelRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerAdapter extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ServerAdapter.class);

    private HandlerDispatcher handlerDispatcher;

    public ServerAdapter(HandlerDispatcher handlerDispatcher) {
        this.handlerDispatcher = handlerDispatcher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageReq req = (MessageReq) msg;
        ChannelRequest creq = new ChannelRequest(ctx.channel(), req);
        this.handlerDispatcher.addMessage(ctx.channel(), creq);
        logger.info("request cmd :[" + req.getCmd() + "] is reading!");
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("ServerAdapter handler error,msg：" + cause.getMessage());
        ctx.close();
    }


}
