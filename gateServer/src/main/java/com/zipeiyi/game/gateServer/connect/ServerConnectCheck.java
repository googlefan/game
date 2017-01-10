package com.zipeiyi.game.gateServer.connect;

import com.zipeiyi.game.gateServer.config.GateServerConfigUpload;
import com.zipeiyi.game.gateServer.domain.KeepAliveMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangxiaoqiang on 16/12/7.
 */
public class ServerConnectCheck implements Runnable{

    @Override
    public void run() {
         while(true){
             ConcurrentHashMap<Integer/**channel hash code**/,KeepAliveMessage.KeepAliveInfo>  keepAliveMap = KeepAliveMessage.getKeepAliveMap();
             GateServerConfigUpload.GateServerUpdate(keepAliveMap.size());
             try {
                 Thread.sleep(1000*120);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }
}
