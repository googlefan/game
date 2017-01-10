package com.common.nio.processor;

import java.util.HashMap;
import java.util.Map;

public class ProcessorConfig {

	public static ProcessorConfig instance ;
	//请求时间间隔
	private Map<Integer,Long> processorInterval = new HashMap<>();
	private Map<Integer,Integer> oprations = new HashMap<>();
	private Map<Integer,Integer> tooFastTime = new HashMap<>();
 
	
	
	public static ProcessorConfig getInstance(){
		
		instance = new ProcessorConfig();
		return instance;
	}


	public Map<Integer, Long> getProcessorInterval() {
		return processorInterval;
	}


	public void setProcessorInterval(Map<Integer, Long> processorInterval) {
		this.processorInterval = processorInterval;
	}


	public Map<Integer, Integer> getOprations() {
		return oprations;
	}


	public void setOprations(Map<Integer, Integer> oprations) {
		this.oprations = oprations;
	}


	public Map<Integer, Integer> getTooFastTime() {
		return tooFastTime;
	}


	public void setTooFastTime(Map<Integer, Integer> tooFastTime) {
		this.tooFastTime = tooFastTime;
	}



	
}
