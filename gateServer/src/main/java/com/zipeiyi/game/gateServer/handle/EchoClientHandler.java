package com.zipeiyi.game.gateServer.handle;

import com.zipeiyi.core.service.IdCenterService;
import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.code.WSCoderService;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.model.UserGateLocation;
import com.zipeiyi.game.common.model.UserLocation;
import com.zipeiyi.game.common.proto.gate.ClientBeatAck;
import com.zipeiyi.game.common.util.RedisUtil;
import com.zipeiyi.game.gateServer.config.GameServerConfigLoad;
import com.zipeiyi.game.gateServer.domain.KeepAliveMessage;
import com.zipeiyi.game.gateServer.netty.NettyClient;
import com.zipeiyi.game.gateServer.netty.NettyClientStart;
import com.zipeiyi.xpower.configcenter.ConfigCenter;
import com.zipeiyi.xpower.configcenter.SystemConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zhangxiaoqiang on 16/12/5.
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);

//    static final String ECHO_REQ = "Hi,Please beat me";
//    static final String ECHO_RES = "Hi,Welcome to GateServer";

    // 失败计数器：未收到client端发送的ping请求
    private int unRecPingTimes = 0 ;

    private int MAX_UN_REC_PING_TIMES = 3;

    private IdCenterService idCenterService;

    public EchoClientHandler(IdCenterService idCenterService){
        this.idCenterService = idCenterService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageReq message = (MessageReq)msg;
        //server心跳客户端响应
        if(message.getCmd() == Constants.SERVER_BEAT_COMMOND){
            unRecPingTimes = 0;
        }else if(message.getCmd() == Constants.CLIENT_BEAT_COMMOND){
            MessageRes cmsg = new MessageRes();
            cmsg.setModuleId(message.getModuleId());
            cmsg.setCmd(Constants.CLIENT_BEAT_COMMOND);
            cmsg.setUid(message.getUid());
            cmsg.setCode(Constants.SUCCESS_CODE);
            cmsg.setMsg(Constants.SUCCESS_MSG);
            WSCoderService.senMessageToClient(ctx.channel(), cmsg);
        }else{
            //非心跳包,uid必须传
            if(message.getUid() <= 0){
                MessageRes cmsg = new MessageRes();
                cmsg.setCmd(message.getCmd());
                cmsg.setModuleId(message.getModuleId());
                cmsg.setUid(message.getUid());
                cmsg.setCode(Constants.PARAM_ERROR_CODE);
                cmsg.setMsg(Constants.PARAM_ERROR_MSG);
                WSCoderService.senMessageToClient(ctx.channel(), cmsg);
            }else{
                //用户连接gateServer缓存
                UserGateLocation userGateLocation = new UserGateLocation();
                userGateLocation.setHostName(SystemConfig.getInstance().getString("GATE_NAME",null));
                userGateLocation.setUid(message.getUid());
                RedisUtil.getRedis().set(Constants.USER_GATE_SERVER_LOCATION_KEY + message.getUid(),10*60,userGateLocation);
                ctx.fireChannelRead(msg);
            }

        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx,evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                    /*读超时*/
                logger.warn("===gate server===(READER_IDLE 读超时)");
                // 失败计数器次数大于等于2次的时候，关闭链接，等待client重连
                if(unRecPingTimes >= MAX_UN_REC_PING_TIMES){
                    logger.error("===gate server===(读超时，关闭chanel),channel hashcode:{}", ctx.channel().hashCode());
                    // 连续超过N次未收到client的ping消息，那么关闭该通道，等待client重连,同时通知gameServer掉线
                    KeepAliveMessage.KeepAliveInfo keepAliveInfo = KeepAliveMessage.getKeepAlive(ctx.channel().hashCode());
                    if(keepAliveInfo != null){
                       //通知game server掉线
                       GameServerConfigLoad.GameServer gs = null;
                       UserLocation userLocation = (UserLocation) RedisUtil.getRedis().get(Constants.USER_GAME_SERVER_LOCATION_KEY + keepAliveInfo.uid);
                       List<GameServerConfigLoad.GameServer> gameServers =  GameServerConfigLoad.get().gameServerList;
                       for(GameServerConfigLoad.GameServer gameServer : gameServers){
                           if(gameServer.hostName.equals(userLocation.getHostName())){
                               gs = gameServer;
                               break;
                           }
                       }
                       if(gs != null){
                           //通知掉线
                           try{
                               while(true){
                                   NettyClient nettyClient = NettyClientStart.getNettyClient(gs.ip, gs.port);
                                   MessageReq messageReq = new MessageReq();
                                   messageReq.setSeque(idCenterService.getId(String.valueOf(System.currentTimeMillis())));
                                   messageReq.setCmd(Constants.OFFLINE_NOTIFY);
                                   messageReq.setUid(keepAliveInfo.uid);
                                   MessageRes response = nettyClient.sent(messageReq);
                                   if(response.getCode() == 1)
                                       break;
                               }
                               //通知掉线后，需要将用户与对应gate server缓存删除
                               RedisUtil.getRedis().delete(Constants.USER_GATE_SERVER_LOCATION_KEY + keepAliveInfo.uid);
                           }catch(Exception e){
                               logger.error("gate notify offline to game server error,",e);
                           }
                       }
                       KeepAliveMessage.removeKeepAliveInfo(ctx.channel());
                    }
                    ctx.channel().close();
                }else{
                    // 失败计数器加1
                    unRecPingTimes++;
                }
            } else if (event.state() == IdleState.WRITER_IDLE) {
                    /*写超时*/
                logger.warn("===gate server===(WRITER_IDLE 写超时)");
            } else if (event.state() == IdleState.ALL_IDLE) {
                    /*读写idle超时,向客户端发送心跳包，等待读取*/
                logger.warn("===gate server===(ALL_IDLE 总超时)");
                MessageRes message = new MessageRes();
                message.setCode(Constants.SUCCESS_CODE);
                message.setMsg(Constants.SUCCESS_MSG);
                message.setCmd(Constants.SERVER_BEAT_COMMOND);
                WSCoderService.senMessageToClient(ctx.channel(), message);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("EchoClientHandler handler error,msg："+cause.getMessage());
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
//        ctx.channel().close();
        logger.info("客户端失效，channel关闭");
    }
}
