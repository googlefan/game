package com.zipeiyi.game.gateServer.domain;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangxiaoqiang on 16/12/5.
 */
public class KeepAliveMessage {

    private static final Logger logger = LoggerFactory.getLogger(KeepAliveMessage.class);

    private static final ConcurrentHashMap<Integer/**channel hash code**/,KeepAliveInfo> keepAliveMap = new ConcurrentHashMap<Integer,KeepAliveInfo>();

    public static KeepAliveInfo getKeepAlive(int hashCode){
        return keepAliveMap.get(hashCode);
    }

    public static void addKeepAlive(KeepAliveInfo keepAliveInfo){
        logger.info("gate server add keep alive channel,channel code:{},keepAliveInfo:{}",keepAliveInfo.gateChannel.hashCode(),keepAliveInfo);
        keepAliveMap.putIfAbsent(keepAliveInfo.gateChannel.hashCode(),keepAliveInfo);
        //如果原来有当前uid,对应channel不同，需要覆盖
//        KeepAliveInfo kai = getKeepLiveByUid(keepAliveInfo.uid);
//        if(kai != null && kai.gateChannel.hashCode() != keepAliveInfo.gateChannel.hashCode()){
//           removeKeepAliveInfo(kai.gateChannel);
//        }
    }

    public static void removeKeepAliveInfo(Channel channel){
        logger.info("gate server remover keep alive channel,channel code:{}",channel.hashCode());
        keepAliveMap.remove(channel.hashCode());
    }

    public static class KeepAliveInfo{
        public Channel gateChannel;//gateServer通道
        public long uid; //用户uid
		@Override
		public String toString() {
			return "KeepAliveInfo [gateChannel=" + gateChannel + ", uid=" + uid + "]";
		}
        
        
        
    }


    public static ConcurrentHashMap<Integer/**channel hash code**/,KeepAliveInfo> getKeepAliveMap(){
        return keepAliveMap;
    }

    
   
    public static KeepAliveInfo getKeepLiveByUid(long uid){
       Iterator<Map.Entry<Integer, KeepAliveInfo>> it  =  keepAliveMap.entrySet().iterator();
       while(it.hasNext()){
           Map.Entry<Integer/**channel hash code**/,KeepAliveInfo> entry =  it.next();
           if(entry.getValue() != null && entry.getValue().uid == uid){
               return entry.getValue();
           }
       }
       return null;
    }
}
