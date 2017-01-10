package com.game.event;

public class ServiceEngine implements IQServiceSink{

	private CQService service;
	
	public static void start(){
		
	}
	
	public static void stop(){
		
	}

	public CQService getService() {
		return service;
	}

	public void setService(CQService service) {
		this.service = service;
	}
	
	
}
