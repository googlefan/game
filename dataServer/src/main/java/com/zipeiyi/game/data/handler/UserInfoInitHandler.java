package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBUserInfoAck;
import com.zipeiyi.game.common.proto.DBUserInfoReq;
import com.zipeiyi.game.common.proto.pojo.GameUserInfo;
import com.zipeiyi.game.common.util.CmdSignUtil;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * Created by zhuhui on 16-12-29.
 */
public class UserInfoInitHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 用户数据初始化！");
        UserService userService = NettyServerStart.factory.getBean(UserService.class);
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("用户数据初始化！");
        String s = CmdSignUtil.getUserInfoSign(request.getUid(), request.getTime());
        DBUserInfoReq obj = (DBUserInfoReq) request.getObj();
        userService.initUserProfile(obj.getUid());
        DBUserInfoAck ack = new DBUserInfoAck();
        GameUserInfo gameUserInfo = new GameUserInfo();
        ack.setUserInfo(gameUserInfo);
        response.setObj(ack);
    }
}
