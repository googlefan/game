package com.zipeiyi.game.data.handler;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;
import com.zipeiyi.game.common.proto.DBCardListAck;
import com.zipeiyi.game.common.proto.pojo.CardInfo;
import com.zipeiyi.game.common.util.CmdSignUtil;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.model.Card;
import com.zipeiyi.game.data.service.UpidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;

public class InitHandler implements GameHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void execute(MessageReq request, MessageRes response) throws ParseException {
        this.logger.info("[" + request.getCmd() + "] 用户数据初始化赠送UPID！");
        UpidService upidService = NettyServerStart.factory.getBean(UpidService.class);
        response.setModuleId(request.getModuleId());
        response.setCmd(request.getCmd());
        response.setSeque(request.getSeque());
        response.setUid(request.getUid());
        response.setMsg("用户数据初始化赠送UPID！");
        String s = CmdSignUtil.getUserInfoSign(request.getUid(), request.getTime());
        List<Card> cardList = upidService.regGiftUpid(request.getUid());
        DBCardListAck ack = new DBCardListAck();
        List<CardInfo> cardInfoList = upidService.castCard2CardInfo(cardList);
        ack.setCardList(cardInfoList);
        response.setObj(ack);
    }


}
