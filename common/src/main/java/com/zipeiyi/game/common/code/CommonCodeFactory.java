package com.zipeiyi.game.common.code;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class CommonCodeFactory extends LengthFieldBasedFrameDecoder{

	private ServerDecoder decoder;
	private ServerEncoder encoder;
	
	private ClientEncoder dbEncoder;
	private ClientDecoder dbDecoder;
	private static CommonCodeFactory instance;
	
	public static CommonCodeFactory getInstance(){
		if(instance == null)
			instance = new CommonCodeFactory();
		return instance;
	}
	
	
	public CommonCodeFactory() {

		super(1024 * 1024, 0, 4, 0, 4);

		decoder = new ServerDecoder();
		encoder = new ServerEncoder();
		
		dbEncoder = new ClientEncoder();
		dbDecoder = new ClientDecoder();
	}

	public ServerDecoder getDecoder() {
		return decoder;
	}

	public void setDecoder(ServerDecoder decoder) {
		this.decoder = decoder;
	}

	public ServerEncoder getEncoder() {
		return encoder;
	}

	public void setEncoder(ServerEncoder encoder) {
		this.encoder = encoder;
	}


	public ClientEncoder getDbEncoder() {
		return dbEncoder;
	}


	public void setDbEncoder(ClientEncoder dbEncoder) {
		this.dbEncoder = dbEncoder;
	}


	public ClientDecoder getDbDecoder() {
		return dbDecoder;
	}


	public void setDbDecoder(ClientDecoder dbDecoder) {
		this.dbDecoder = dbDecoder;
	}
	
	
}
