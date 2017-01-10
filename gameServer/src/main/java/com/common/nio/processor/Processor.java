package com.common.nio.processor;

import com.common.nio.socket.Session;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;

public abstract class Processor {


	private int cmd;
	public abstract void process(Session session,MessageReq request,MessageRes response) throws Exception;
	
	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}


}
