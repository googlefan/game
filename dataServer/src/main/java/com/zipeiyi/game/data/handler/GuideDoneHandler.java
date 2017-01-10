package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBUerGuidDoneAck;
import com.zipeiyi.game.common.proto.gate.GameDirectAck;
import com.zipeiyi.game.common.util.CmdSignUtil;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * [1003]新手引导完成
 * Created by zhuhui on 17-1-2.
 */
public class GuideDoneHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 新手引导完成！");
        UserService userService = NettyServerStart.factory.getBean(UserService.class);
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("新手引导完成！");
        String s = CmdSignUtil.getUserInfoSign(request.getUid(), request.getTime());
        Integer code = userService.updateUserInitStats(request.getUid());
        response.setCode(code);
        GameDirectAck ack = new GameDirectAck();
        ack.setAckResult(code);
        response.setObj(ack);
    }
}
