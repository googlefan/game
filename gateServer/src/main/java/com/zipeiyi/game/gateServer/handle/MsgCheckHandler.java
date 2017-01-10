package com.zipeiyi.game.gateServer.handle;

import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.code.WSCoderService;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBHallUserInfoReq;
import com.zipeiyi.game.gateServer.domain.KeepAliveMessage;
import com.zipeiyi.game.gateServer.netty.NettyClient;
import com.zipeiyi.game.gateServer.netty.NettyClientStart;
import com.zipeiyi.game.gateServer.service.antispam.Frequency;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangxiaoqiang on 16/12/5.
 */
public class MsgCheckHandler extends SimpleChannelInboundHandler<Object> {

    private Logger logger = LoggerFactory.getLogger(MsgCheckHandler.class);

    private static final String USER_REQUEST_COMMAND_LIMIT_KEY = "freq_user_";

    private static final int LIMIT_TIMES = 100;

    private static final int LIMIT_TIME = 30;

    private Frequency frequency;

    public MsgCheckHandler(Frequency frequecncy){
        this.frequency = frequecncy;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageReq message = (MessageReq)msg;
        //包频率限制
//        if(message.getCmd() == 1002){
//            NettyClient nettyClient = NettyClientStart.getNettyClient("192.169.20.143",8000);
//            MessageReq req = new MessageReq();
//            req.setCmd(9001);
//            req.setUid(559175277402660864l);
//            req.setChannelCode(200000);
//            req.setModuleId(0);
//            req.setSeque(System.currentTimeMillis());
//            DBHallUserInfoReq dbHallUserInfoReq = new DBHallUserInfoReq();
//            dbHallUserInfoReq.setUid(559175277402660864l);
//            req.setObj(dbHallUserInfoReq);
//            MessageRes res = nettyClient.sent(req);
//            res.setCmd(1002);
//            WSCoderService.senMessageToClient(ctx.channel(), res);
//        } else
            if(message.getUid() <= 0 || StringUtils.isEmpty(message.getToken())){
            //无效的请求
            logger.error("客户端发送请求参数错误，requestMsg:{}",message.toString());
            MessageRes msgRes = new MessageRes();
            msgRes.setCmd(message.getCmd());
            msgRes.setModuleId(message.getModuleId());
            msgRes.setUid(message.getUid());
            msgRes.setCode(Constants.PARAM_ERROR_CODE);
            msgRes.setMsg(Constants.PARAM_ERROR_MSG);
            WSCoderService.senMessageToClient(ctx.channel(), msgRes);
        }//指令限制
        else if(!Constants.COMMONIDS.contains(message.getCmd())){
            logger.error("客户端发送请求指令错误，requestMsg:{}",message);
            MessageRes msgRes = new MessageRes();
            msgRes.setCmd(message.getCmd());
            msgRes.setModuleId(message.getModuleId());
            msgRes.setUid(message.getUid());
            msgRes.setCode(Constants.ERROR_COMMAND);
            msgRes.setMsg(Constants.PARAM_ERROR_MSG);
            WSCoderService.senMessageToClient(ctx.channel(), msgRes);
        }//30s,req 100 limit
        else if(!frequency.addTimes(USER_REQUEST_COMMAND_LIMIT_KEY + message.getUid(),LIMIT_TIMES,LIMIT_TIME)){
            logger.error("客户端用户请求频率过于频繁，可能作弊请求，requestMsg:{}",message);
            MessageRes msgRes = new MessageRes();
            msgRes.setModuleId(message.getModuleId());
            msgRes.setCmd(message.getCmd());
            msgRes.setUid(message.getUid());
            msgRes.setCode(Constants.FREQUENCY_ERROR_CODE);
            msgRes.setMsg(Constants.PARAM_ERROR_MSG);
            WSCoderService.senMessageToClient(ctx.channel(), msgRes);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("MsgCheckHandler handler error,msg："+ cause.getMessage());
//        KeepAliveMessage.removeKeepAliveInfo(ctx.channel());
//        ctx.channel().close();
    }

}

