package com.zipeiyi.game.gateServer.netty;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;

/**
 * Created by zhangxiaoqiang on 16/12/2.
 */
public interface Client {

    MessageRes sent(MessageReq request) throws Exception;
    void close();
}
