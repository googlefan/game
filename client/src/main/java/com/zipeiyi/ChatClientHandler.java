package com.zipeiyi;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.proto.DBCardListReq;
import com.zipeiyi.game.common.proto.DBUserInfoReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by zhuhui on 16-12-5.
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    protected void channelRead0(ChannelHandlerContext chc, String s) throws Exception {
        MessageReq req = getMessageReq2();
        chc.channel().writeAndFlush(req);
        System.out.println(s);
    }



    private MessageReq getMessageReq() {
        MessageReq req = new MessageReq();
        req.setUid(558556983851229184l);
        req.setCmd(9100);
        req.setModuleId(1);
        DBUserInfoReq dbUserInfoReq = new DBUserInfoReq();
        dbUserInfoReq.setUid(558556983851229184l);
        req.setObj(dbUserInfoReq);
        req.setSeque(1);
        return req;
    }

    private MessageReq getMessageReq1() {
        MessageReq req = new MessageReq();
        req.setUid(558556983851229184l);
        req.setCmd(9101);
        req.setModuleId(1);
//        DBUserCardReq dbUserInfoReq = new DBUserCardReq();
//        dbUserInfoReq.setUid(558556983851229184l);
//        req.setObj(dbUserInfoReq);
        req.setSeque(1);
        return req;
    }

    private MessageReq getMessageReq2() {
        MessageReq req = new MessageReq();
        req.setUid(5585569838512291840l);
        req.setCmd(9102);
        req.setModuleId(1);
        DBCardListReq dbCardListReq = new DBCardListReq();
        dbCardListReq.setCardAmount(12);
        req.setObj(dbCardListReq);
        req.setSeque(1);
        return req;
    }

    private MessageReq getMessageReq3() {
        MessageReq req = new MessageReq();
        req.setUid(558556983851229184l);
        req.setCmd(9103);
        req.setModuleId(1);
        DBUserInfoReq dbUserInfoReq = new DBUserInfoReq();
        dbUserInfoReq.setUid(558556983851229184l);
        req.setObj(dbUserInfoReq);
        req.setSeque(1);
        return req;
    }

    private MessageReq getMessageReq4() {
        MessageReq req = new MessageReq();
        req.setUid(558556983851229184l);
        req.setCmd(9001);
        req.setModuleId(1);
        DBUserInfoReq dbUserInfoReq = new DBUserInfoReq();
        dbUserInfoReq.setUid(558556983851229184l);
        req.setObj(dbUserInfoReq);
        req.setSeque(1);
        return req;
    }

    private MessageReq getMessageReq5() {
        MessageReq req = new MessageReq();
        req.setUid(558556983851229184l);
        req.setCmd(9002);
        req.setModuleId(1);
        DBUserInfoReq dbUserInfoReq = new DBUserInfoReq();
        dbUserInfoReq.setUid(558556983851229184l);
        req.setObj(dbUserInfoReq);
        req.setSeque(1);
        return req;
    }

    private MessageReq getMessageReq6() {
        MessageReq req = new MessageReq();
        req.setUid(558556983851229184l);
        req.setCmd(9003);
        req.setModuleId(1);
        DBUserInfoReq dbUserInfoReq = new DBUserInfoReq();
        dbUserInfoReq.setUid(558556983851229184l);
        req.setObj(dbUserInfoReq);
        req.setSeque(1);
        return req;
    }
}
