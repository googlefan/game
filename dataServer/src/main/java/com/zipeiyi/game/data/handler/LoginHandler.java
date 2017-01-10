package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.dao.UserDao;
import com.zipeiyi.game.data.dao.impl.UserDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhuhui on 16-12-6.
 */
public class LoginHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq req, MessageRes res) {
        this.logger.info(req.toString());
    }
}
