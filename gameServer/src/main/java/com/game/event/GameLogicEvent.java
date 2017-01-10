package com.game.event;



import com.common.nio.socket.Session;
import com.game.constant.Define;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;

public class GameLogicEvent extends Event{
	
	Define.GameOperatioinEnum operatioin;	
	MessageReq request;
	Session session;
	MessageRes response;
	String tableID;
	
	
	public Define.GameOperatioinEnum getOperatioin() {
		return operatioin;
	}
	public void setOperatioin(Define.GameOperatioinEnum operatioin) {
		this.operatioin = operatioin;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}

	
	public MessageReq getRequest() {
		return request;
	}
	public void setRequest(MessageReq request) {
		this.request = request;
	}
	public MessageRes getResponse() {
		return response;
	}
	public void setResponse(MessageRes response) {
		this.response = response;
	}
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	
	

}
