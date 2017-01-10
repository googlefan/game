package com.game.constant;

import java.util.Map;

import com.google.common.collect.Maps;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;

public class DBParam {

	MessageReq request;
	
	MessageRes response;

	Map<String,Object> reqDataMap = Maps.newHashMap();
	

	public MessageReq getRequest() {
		return request;
	}

	public void setRequest(MessageReq request) {
		this.request = request;
	}

	
	public MessageRes getResponse() {
		return response;
	}

	public Map<String, Object> getReqDataMap() {
		return reqDataMap;
	}

	public void setReqDataMap(Map<String, Object> reqDataMap) {
		this.reqDataMap = reqDataMap;
	}

	public void setResponse(MessageRes response) {
		this.response = response;
	}
	
}
