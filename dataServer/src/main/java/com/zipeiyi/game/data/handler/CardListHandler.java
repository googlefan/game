package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBCardListAck;
import com.zipeiyi.game.common.proto.DBCardListReq;
import com.zipeiyi.game.common.proto.pojo.CardInfo;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;

/**
 * [9102]游戏暗牌
 * Created by zhuhui on 16-12-29.
 */
public class CardListHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 游戏暗牌！");
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("游戏暗牌！");
        DBCardListReq req = (DBCardListReq) request.getObj();
        GameService gameService = NettyServerStart.factory.getBean(GameService.class);
        List<CardInfo> cardInfoList = gameService.getHideCard(req.getCardAmount());

        DBCardListAck ack = new DBCardListAck();
        ack.setCardList(cardInfoList);
        response.setObj(ack);
    }
}
