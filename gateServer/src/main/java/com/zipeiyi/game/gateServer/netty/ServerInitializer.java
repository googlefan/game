package com.zipeiyi.game.gateServer.netty;

import com.zipeiyi.core.service.IdCenterService;
import com.zipeiyi.game.common.code.WSCoderService;
import com.zipeiyi.game.gateServer.handle.*;
import com.zipeiyi.game.gateServer.service.antispam.Frequency;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
	private HandlerDispatcher handlerDispatcher;
    private IdCenterService idCenterService;
    private Frequency frequency;
    private WSCoderService wsCoderService;

    private long reader_idle_time = 250;
    private long writer_idle_time = 200;
    private long reader_and_writer_time = 100;

	public void init() {
		new Thread(this.handlerDispatcher).start();
	}

	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536)); // 定义缓冲大小
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());

        ch.pipeline().addLast("handshake",new HandshakeHandler(wsCoderService));
        ch.pipeline().addLast("idleHandler",new IdleStateHandler(reader_idle_time,writer_idle_time,reader_and_writer_time, TimeUnit.SECONDS));
        //心跳包检测
//        ch.pipeline().addLast("echoClientHandle", new EchoClientHandler(idCenterService));
        //频率错误包处理
        ch.pipeline().addLast("msgCheckHandle",new MsgCheckHandler(frequency));
       //登录验证
        pipeline.addLast("loginCheckHandle",new LoginCheckHandler(idCenterService));
        //消息指令转发处理
		pipeline.addLast("handler", new ServerAdapter(this.handlerDispatcher));
	}

    public void setWsCoderService(WSCoderService wsCoderService) {
        this.wsCoderService = wsCoderService;
    }

    public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

    public void setIdCenterService(IdCenterService idCenterService) {
        this.idCenterService = idCenterService;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public void setReader_idle_time(long reader_idle_time) {
        this.reader_idle_time = reader_idle_time;
    }

    public void setWriter_idle_time(long writer_idle_time) {
        this.writer_idle_time = writer_idle_time;
    }

    public void setReader_and_writer_time(long reader_and_writer_time) {
        this.reader_and_writer_time = reader_and_writer_time;
    }
}