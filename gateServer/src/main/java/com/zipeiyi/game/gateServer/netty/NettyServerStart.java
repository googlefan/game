package com.zipeiyi.game.gateServer.netty;


import com.zipeiyi.game.gateServer.connect.ServerConnectCheck;
import com.zipeiyi.xpower.configcenter.SystemConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by zhangxiaoqiang on 16/11/30.
 */
public class NettyServerStart {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerStart.class);
    public static int port = 8080;

    public static void main(String[] args) throws Exception {
        if(args.length >= 1){
            try{
                port = Integer.parseInt(args[0]);
            }catch(Exception e){
               logger.error("gate server start error,please input correct port,for example:8080");
            }
        }
        //check gateName
        String gateName = SystemConfig.getInstance().getString("GATE_NAME",null);
        if(StringUtils.isBlank(gateName)){
            logger.error("请设置当前gate server name,用于集群负载区别，请参照zk配置，设置参数：-DGATE_NAME=XXX");
            return;
        }
        DOMConfigurator.configureAndWatch("config/log4j.xml");
        serverStart();
    }

    public static void serverStart() throws Exception {
        ApplicationContext context = new FileSystemXmlApplicationContext("config/propholder.xml");
        ServerInitializer initializer = (ServerInitializer)context.getBean(ServerInitializer.class);
        new Thread(new ServerConnectCheck()).start();
        logger.info("gate server connect check running...");

        logger.info("gate server is running...");
        NettyServer server = new NettyServer(port);
        logger.info("gate server start use port:" + port);
        server.setInitializer(initializer);
        server.run();

    }
}
