package com.zipeiyi.game.gateServer.handle;

import com.zipeiyi.core.service.IdCenterService;
import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.code.WSCoderService;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBHallUserInfoReq;
import com.zipeiyi.game.common.util.RandomUtil;
import com.zipeiyi.game.gateServer.config.DBServerConfigLoad;
import com.zipeiyi.game.gateServer.domain.KeepAliveMessage;
import com.zipeiyi.game.gateServer.netty.NettyClient;
import com.zipeiyi.game.gateServer.netty.NettyClientStart;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zhangxiaoqiang on 16/12/2.
 */
public class LoginCheckHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(LoginCheckHandler.class);

    private IdCenterService idCenterService;

    public LoginCheckHandler(IdCenterService idCenterService){
        this.idCenterService = idCenterService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageReq message = (MessageReq)msg;
        int cmd = message.getCmd();
        //验证登录请求的正确性
        try{
            List<DBServerConfigLoad.DBServer> dbServers = DBServerConfigLoad.get().dbServerList;
            int index = RandomUtil.generateRandomIndex(dbServers.size());
            NettyClient nettyClient = NettyClientStart.getNettyClient(dbServers.get(index).ip,dbServers.get(index).port);
            MessageReq mr = new MessageReq();
//            mr.setToken(message.getToken());
//            mr.setUid(message.getUid());
//            mr.setTime(message.getTime());
            mr = message.clone();
            mr.setCmd(Constants.REQUEST_TOKEN_VERIFY);
            mr.setObj(null);
            mr.setSeque(idCenterService.getId(String.valueOf(System.currentTimeMillis())));
            MessageRes response = nettyClient.sent(mr);
            if(response.getCode() == Constants.SUCCESS_CODE){
                logger.info("=======加密指令验证成功=========");
                //1002,1003请求大厅指令需要转发给dbserver处理
                if(cmd == Constants.FETCH_GAME_ROLE_INFO || cmd == Constants.USER_GAME_DIRECT_NOTIFY){
                    message.setCmd(cmd);
                    message.setSeque(idCenterService.getId(String.valueOf(System.currentTimeMillis())));
                    MessageRes responseFromDb = nettyClient.sent(message);
                    WSCoderService.senMessageToClient(ctx.channel(), responseFromDb);
                }else{
                    message.setCmd(cmd);
                    //验证通过处理，继续转发
                    ctx.fireChannelRead(message);
                }
            }else{
                logger.info("=======加密指令验证失败=========");
                MessageRes messageRes = new MessageRes();
                message.setCmd(cmd);
                messageRes.setModuleId(message.getModuleId());
                messageRes.setUid(message.getUid());
                messageRes.setCode(Constants.TOKEN_VERIFY_ERROR_CODE);
                messageRes.setMsg(Constants.TOKEN_VERIFY_ERROR);
                WSCoderService.senMessageToClient(ctx.channel(), messageRes);
            }
        }catch(Exception e){
            logger.error("=====dbserver cmd communicate errror=====,",e);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("LoginCheck handler error,msg："+cause.getMessage());
//        KeepAliveMessage.removeKeepAliveInfo(ctx.channel());
//        ctx.channel().close();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 关闭，等待重连
        KeepAliveMessage.removeKeepAliveInfo(ctx.channel());
//        ctx.close();
        logger.info("客户端失效，channel关闭");
    }

}
