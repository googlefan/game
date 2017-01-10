package com.zipeiyi;

import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.proto.DBCardListReq;
import com.zipeiyi.game.common.proto.DBUserInfoReq;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

/**
 * Created by zhuhui on 16-12-5.
 */
public class ChatClient {
    private final String host;
    private final int port;

    public static void main(String[] args) throws IOException, InterruptedException {
//        new ChatClient("192.169.30.16", 8000).run();
        new ChatClient("127.0.0.1", 8000).run();
    }

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatChannelInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();

            MessageReq req = getMessageReq2();
            channel.writeAndFlush(req);

            Thread.sleep(3000);
            MessageReq req3 = getMessageReq3();
            channel.writeAndFlush(req3);

            //channel.closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            //group.shutdownGracefully();
        }
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
