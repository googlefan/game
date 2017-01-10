package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBHallUserInfoAck;
import com.zipeiyi.game.common.proto.DBHallUserInfoReq;
import com.zipeiyi.game.common.proto.pojo.UserInfo;
import com.zipeiyi.game.common.util.CmdSignUtil;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * [9001]大厅获取用户数据
 * Created by zhuhui on 16-12-29.
 */
public class HallUserInfoHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 大厅获取用户数据！");
        UserService userService = NettyServerStart.factory.getBean(UserService.class);
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("大厅获取用户数据！");
        String s = CmdSignUtil.getUserInfoSign(request.getUid(), request.getTime());
        DBHallUserInfoReq reqBean = (DBHallUserInfoReq) request.getObj();
        UserInfo userInfo = userService.getUserInfo(reqBean.getUid());
        DBHallUserInfoAck ack = new DBHallUserInfoAck();
        ack.setUserInfo(userInfo);
        response.setObj(ack);
    }
}
