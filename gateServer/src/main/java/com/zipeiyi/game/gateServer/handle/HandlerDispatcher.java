package com.zipeiyi.game.gateServer.handle;

import com.zipeiyi.core.service.IdCenterService;
import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.code.WSCoderService;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.model.UserLocation;
import com.zipeiyi.game.common.proto.DBHallUserInfoReq;
import com.zipeiyi.game.common.util.JsonUtil;
import com.zipeiyi.game.common.util.RedisUtil;
import com.zipeiyi.game.gateServer.config.GameServerConfigLoad;
import com.zipeiyi.game.gateServer.domain.KeepAliveMessage;
import com.zipeiyi.game.gateServer.domain.MessageQueue;
import com.zipeiyi.game.gateServer.netty.NettyClient;
import com.zipeiyi.game.gateServer.netty.NettyClientStart;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class HandlerDispatcher implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(HandlerDispatcher.class);

	private final Map<Integer, MessageQueue> sessionMsgQ;
	private Executor messageExecutor;
	private boolean running;
	private long sleepTime;

    private IdCenterService idCenterService;

	public HandlerDispatcher() {
		this.sessionMsgQ = new ConcurrentHashMap<Integer, MessageQueue>();
		this.running = true;
		this.sleepTime = 200L;
	}


	public void setMessageExecutor(Executor messageExecutor) {
		this.messageExecutor = messageExecutor;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void addMessageQueue(int channelHashcode, MessageQueue messageQueue) {
		this.sessionMsgQ.put(channelHashcode, messageQueue);
	}

	public void removeMessageQueue(int channelHashcode) {
		MessageQueue queue = (MessageQueue) this.sessionMsgQ.remove(channelHashcode);
		if (queue != null)
			queue.clear();
	}

	public void addMessage(MessageReq request) {
		try {
			MessageQueue messageQueue = (MessageQueue) this.sessionMsgQ
					.get(request.getChannelCode());

			if (messageQueue == null) {
				messageQueue = new MessageQueue(new ConcurrentLinkedQueue<MessageReq>());

				this.sessionMsgQ.put(request.getChannelCode(), messageQueue);
				messageQueue.add(request);
			} else {
				messageQueue.add(request);
			}
		} catch (Exception e) {
			HandlerDispatcher.logger.error("gate server消息添加异常，",e);
		}
	}

	public void run() {
		while (this.running) {
			try {
				for (MessageQueue messageQueue : sessionMsgQ.values())
					if ((messageQueue != null) && (messageQueue.size() > 0) && (!messageQueue.isRunning())) {
						MessageWorker messageWorker = new MessageWorker(messageQueue);

						this.messageExecutor.execute(messageWorker);
					}
			} catch (Exception e) {
				HandlerDispatcher.logger.error("消息队列异步处理异常，",e);
			}
			try {
				Thread.sleep(this.sleepTime);
			} catch (InterruptedException e) {
				HandlerDispatcher.logger.error("消息队列处理，sleep中断异常",e);
			}
		}
	}

	public void stop() {
		this.running = false;
	}

	public MessageQueue getUserMessageQueue(Channel channel) {
		return (MessageQueue) this.sessionMsgQ.get(channel);
	}

	private final class MessageWorker implements Runnable {
		private final MessageQueue messageQueue;
		private MessageReq request;

		private MessageWorker(MessageQueue messageQueue) {
			messageQueue.setRunning(true);
			this.messageQueue = messageQueue;
			this.request = ((MessageReq) messageQueue.getRequestQueue().poll());
		}

		public void run() {
			try {
				handMessageQueue();
			} catch (Exception e) {
				HandlerDispatcher.logger.error("gameServer消息处理异常,",e);
			} finally {
				this.messageQueue.setRunning(false);
			}
		}

		private void handMessageQueue() {
            //设置messageId并发送消息，连接地址首先通过redis获取
            UserLocation userLocation = (UserLocation)RedisUtil.getRedis().get(Constants.USER_GAME_SERVER_LOCATION_KEY + request.getUid());
            List<GameServerConfigLoad.GameServer> gameServers =  GameServerConfigLoad.get().gameServerList;
            if(userLocation == null){
                //已经掉线或者退出房间，按照zk实时连接负载信息连接
                Collections.sort(gameServers,new Comparator<GameServerConfigLoad.GameServer>() {
                    @Override
                    public int compare(GameServerConfigLoad.GameServer o1, GameServerConfigLoad.GameServer o2) {
                        if(o1.connect - o2.connect >= 0){
                            return 1;
                        }else{
                            return -1;
                        }
                    }
                });
                GameServerConfigLoad.GameServer gameServer = gameServers.get(0);
                messageHandle(gameServer);
            }else{
                //掉线重连或者活跃状态已经分配房间，再次连接进入同一房间（服务器）
                GameServerConfigLoad.GameServer gs = null;
                for(GameServerConfigLoad.GameServer gameServer : gameServers){
                    if(gameServer.hostName.equals(userLocation.getHostName())){
                       gs = gameServer;
                       break;
                    }
                }
                if(gs != null){
                    messageHandle(gs);
                }else{
                   logger.error("gate server select game server not match,game server hostName:{}",userLocation.getHostName());
                }
            }
		}

        private void messageHandle(GameServerConfigLoad.GameServer gameServer){
            try{
                NettyClient nettyClient = NettyClientStart.getNettyClient(gameServer.ip, gameServer.port);
                request.setSeque(idCenterService.getId(String.valueOf(System.currentTimeMillis())));
				logger.info("=========gameserver handler send message:{}",JsonUtil.getJsonFromBean(request));
                MessageRes res = nettyClient.sent(request);
                logger.info("=========gameserver handler message response:{}",JsonUtil.getJsonFromBean(res));
                KeepAliveMessage.KeepAliveInfo keepAliveInfo = KeepAliveMessage.getKeepAlive(request.getChannelCode());
    	        logger.info("use keepAliveInfo channel code:{}",request.getChannelCode());
                if(keepAliveInfo != null && keepAliveInfo.gateChannel != null && keepAliveInfo.gateChannel.isOpen() && keepAliveInfo.gateChannel.isActive()){
                    WSCoderService.senMessageToClient(keepAliveInfo.gateChannel, res);
                }else{
                    logger.error("=========gate channel not found info keepAliveInfo,can not send game server reponse to client,response:{}",res);
                }
            }catch(Exception e){
                logger.error("gate handle message between gate server and game server error,",e);
            }
        }
    }

	public IdCenterService getIdCenterService() {
		return idCenterService;
	}


	public void setIdCenterService(IdCenterService idCenterService) {
		this.idCenterService = idCenterService;
	}
	
	
}
