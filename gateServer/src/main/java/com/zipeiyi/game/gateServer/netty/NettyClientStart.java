package com.zipeiyi.game.gateServer.netty;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangxiaoqiang on 16/12/2.
 */
public class NettyClientStart {

    private static final ConcurrentHashMap<String, NettyClient> nettyClientMap = new ConcurrentHashMap<>();

    /**
     * 按照ip+port获取连接
     * @param ip
     * @param port
     * @return
     */
    public static synchronized NettyClient getNettyClient(String ip,int port) throws Exception{
        checkKeepLive();
        NettyClient nettyClient = nettyClientMap.get(getNettyClientKey(ip,port));
        if(nettyClient == null){
            nettyClient = new NettyClient(ip,port);
            nettyClientMap.putIfAbsent(getNettyClientKey(ip,port),nettyClient);
        }
        return nettyClient;
    }

    /**
     * 自动的连接监测，掉线重连
     */
    private static void checkKeepLive(){
        Iterator<Map.Entry<String,NettyClient>> it = nettyClientMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,NettyClient> entry = it.next();
            NettyClient nettyClient = (NettyClient)entry.getValue();
            if(nettyClient == null || !nettyClient.checkAlive()){
                it.remove();
                nettyClient.close();
            }
        }
    }

    private static String getNettyClientKey(String ip,int port){
        return ip + ":" + port;
    }
}
