package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.gate.MainGameAck;
import com.zipeiyi.game.common.proto.pojo.UserInfo;
import com.zipeiyi.game.common.util.CmdSignUtil;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * [1002]请求大厅数据
 * Created by zhuhui on 17-1-6.
 */
public class GateHallUserInfoHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 网关大厅获取用户数据！");
        UserService userService = NettyServerStart.factory.getBean(UserService.class);
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("网关大厅获取用户数据！");
        String s = CmdSignUtil.getUserInfoSign(request.getUid(), request.getTime());
        UserInfo userInfo = userService.getUserInfo(request.getUid());
        MainGameAck ack = new MainGameAck();
        ack.setUserInfo(userInfo);
        response.setObj(ack);
    }
}
