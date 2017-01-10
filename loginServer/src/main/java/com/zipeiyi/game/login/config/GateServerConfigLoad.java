package com.zipeiyi.game.login.config;

import com.github.zkclient.IZkDataListener;
import com.zipeiyi.core.common.utils.JSONUtil;
import com.zipeiyi.xpower.Charsets;
import com.zipeiyi.xpower.configcenter.ConfigCenter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

/**
 * Created by zhangxiaoqiang on 16/12/13.
 */
public class GateServerConfigLoad {

    public List<GateServer> gateServerList;

    public static class GateServer{
        public String hostName;
        public String ip;
        public int port;
        public int connect;
    }

    private static final String CONFIG_PATH = "/xpower/game/gate_server_list";

    private static final Logger log = LoggerFactory.getLogger(GateServerConfigLoad.class);

    private volatile static GateServerConfigLoad configInstance;

    static {
        try {
            _loadConfig();
        } catch (Throwable e) {
            log.error("load config error", e);
        }
    }

    public static GateServerConfigLoad get() {
        return configInstance;
    }


    private static void _loadConfig() throws Exception{
        ConfigCenter configCenter = ConfigCenter.getInstance();
        String config = null;
        try{
            config = configCenter.getDataAsString(CONFIG_PATH);
        }catch(Exception e){
            log.error("unable to connect to zookeeper server ...",e);
            config = null;
        }

        log.info("load gate server list config from zk, config string:{}", config);
        if (StringUtils.isEmpty(config)) {
            log.info("config data not found in zk, load default config data from class path");
            URL url = GateServerConfigLoad.class.getResource("/gate_server_list.json");
            if (url != null) {
                config = new String(IOUtils.toByteArray(url.openStream()), "UTF-8");
                log.info("class path default config:{}", config);
            }
        }

        _config(config);

        configCenter.subscribeDataChanges(CONFIG_PATH, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, byte[] data) throws Exception {
                log.info("zk data changed, try reload data");
                String config = Charsets.UTF8.toString(data);
                log.info("load gate server list config change from zk, config string:{}", config);
                _config(config);
                if (log.isDebugEnabled()) {
                    log.debug("reload config data:\n{}", config);
                }
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                //noting to do
            }
        });
    }

    private static void _config(String config) throws Exception {
        if (StringUtils.isEmpty(config)) {
            log.error("empty config json string");
            return;
        }
        configInstance = JSONUtil.fromJSONString(config, GateServerConfigLoad.class);
    }
}
