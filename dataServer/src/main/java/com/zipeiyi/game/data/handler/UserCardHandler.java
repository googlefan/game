package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

/**
 * [9101]游戏中获取卡牌信息
 * del 放在9001中
 * Created by zhuhui on 16-12-29.
 */
public class UserCardHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
//        this.logger.info("[" + request.getCmd() + "] 游戏中获取卡牌信息！");
//        GameService gameService = NettyServerStart.factory.getBean(GameService.class);
//        response.setModuleId(request.getModuleId());
//        response.setCmd(request.getCmd());
//        response.setSeque(request.getSeque());
//        response.setUid(request.getUid());
//        response.setMsg("游戏中获取卡牌信息！");
//        String s = CmdSignUtil.getUserInfoSign(request.getUid(), request.getTime());
//        DBUserCardReq obj = (DBUserCardReq) request.getObj();
//        CardInfo card = gameService.getUserCard(obj.getUid(),obj.getCardID());
//        DBUserCardAck ack = new DBUserCardAck();
//        ack.setCard(card);
//        response.setObj(ack);
    }
}
