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
 * [9100]游戏中获取用户信息
 * Created by zhuhui on 16-12-29.
 */
public class UserInfoHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 游戏中获取用户信息！");
        UserService userService = NettyServerStart.factory.getBean(UserService.class);
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("游戏中获取用户信息！");
        String s = CmdSignUtil.getUserInfoSign(request.getUid(), request.getTime());
        DBUserInfoReq reqBean = (DBUserInfoReq) request.getObj();
        GameUserInfo userInfo = userService.getUserInfo(reqBean.getUid());
        DBUserInfoAck ack = new DBUserInfoAck();
        if (userInfo == null) {
            response.setCode(2);// 用户不存在
            ack.setAckResult(2);
        }
        ack.setUserInfo(userInfo);
        response.setObj(ack);
    }
}
