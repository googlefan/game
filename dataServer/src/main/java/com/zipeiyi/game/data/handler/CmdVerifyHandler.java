package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.Constants;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.gate.EncryptCheckAck;
import com.zipeiyi.game.common.util.CmdSignUtil;
import com.zipeiyi.game.common.util.RedisUtil;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.service.UserService;
import com.zipeiyi.game.data.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * [1001] 指令验证
 * Created by zhuhui on 17-1-2.
 */
public class CmdVerifyHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 指令验证！");
        UserService userService = NettyServerStart.factory.getBean(UserService.class);
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("指令验证！");
        String ticket = RedisUtil.getRedis().get(Constants.USER_BASIC_INFO + request.getUid());
        if (StringUtils.isNull(ticket)) {
            ticket = userService.getUserTicket(request.getUid());
        }
        String s = CmdSignUtil.getCmdSign(request.getUid(), request.getCmd(), request.getTime(), ticket);
        EncryptCheckAck ack = new EncryptCheckAck();
        if (s.equals(request.getToken())) {
            // SUCCESS
            response.setCode(1);
            ack.setAckResult(1);
        } else {
            response.setCode(2);
            ack.setAckResult(2);
        }
        response.setObj(ack);
    }
}
