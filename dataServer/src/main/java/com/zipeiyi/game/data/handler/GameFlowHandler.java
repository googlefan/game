package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBGameBalanceAck;
import com.zipeiyi.game.common.proto.DBGameBalanceReq;
import com.zipeiyi.game.common.proto.pojo.BalanceDBInfo;
import com.zipeiyi.game.common.util.CmdSignUtil;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;

/**
 * [9103]游戏结算信息
 * Created by zhuhui on 16-12-29.
 */
public class GameFlowHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 游戏结果统计！");
        GameService gameService = NettyServerStart.factory.getBean(GameService.class);
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("游戏结果统计！");
        String s = CmdSignUtil.getUserInfoSign(request.getUid(), request.getTime());
        DBGameBalanceReq obj = (DBGameBalanceReq) request.getObj();
        List<BalanceDBInfo> balanceDBInfoList = obj.getBalanceList();
        gameService.insertGameFlow(balanceDBInfoList);
        DBGameBalanceAck ack = new DBGameBalanceAck();
        response.setObj(ack);
    }
}
