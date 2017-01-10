package com.zipeiyi.game.gateServer.handle;

import com.zipeiyi.game.common.code.WSCoderService;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.gateServer.domain.KeepAliveMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by zhangxiaoqiang on 16/12/2.
 */
public class ClientHandlerDispatcher extends SimpleChannelInboundHandler<MessageRes> {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandlerDispatcher.class);

    private final ConcurrentHashMap<Long, BlockingQueue<MessageRes>> responseMap = new ConcurrentHashMap<>();

    public void write(Channel channel,final Object msg){
        if (msg instanceof MessageReq) {
            MessageReq request = (MessageReq) msg;
            responseMap.putIfAbsent(request.getSeque(), new LinkedBlockingQueue<MessageRes>(1));
        }
        channel.writeAndFlush(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRes response) throws Exception {
        //如果gameserver主动向客户端推送消息，则没有messageId
        if(response.getSeque() <= 0 && CollectionUtils.isNotEmpty(response.getUidList())){
            //消息推送处理
            List<Long> uids = response.getUidList();
            for(long uid : uids){
                KeepAliveMessage.KeepAliveInfo keepAliveInfo = KeepAliveMessage.getKeepLiveByUid(uid);
                if(keepAliveInfo != null){
                	WSCoderService.senMessageToClient(keepAliveInfo.gateChannel, response);
//                	keepAliveInfo.senMessageToClient(response);
//                    keepAliveInfo.gateChannel.writeAndFlush(response);
                }else{
                    logger.warn("channel may be close,can not push message to client,message:{}",response.toString());
                }
            }
        }else{
            //普通请求响应处理
            BlockingQueue<MessageRes> queue = responseMap.get(response.getSeque());
            if(queue != null){
                queue.add(response);
            }
        }

    }

    public MessageRes getResponse(final long messageId) throws Exception {
        MessageRes result;
        responseMap.putIfAbsent(messageId, new LinkedBlockingQueue<MessageRes>(1));
        try {
            result = responseMap.get(messageId).take();
        } catch (Exception ex) {
            throw ex;
        } finally {
            responseMap.remove(messageId);
        }
        return result;
    }

    private MessageRes getSystemMessage() throws Exception {
        try {
            return responseMap.get(-1).poll(5, SECONDS);
        } catch (final InterruptedException ex) {
            throw new Exception(ex);
        }
    }
}
