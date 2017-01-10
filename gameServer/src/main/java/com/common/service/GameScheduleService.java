package com.common.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.common.suport.NamedThreadFactory;

import io.netty.channel.nio.NioEventLoop;

/**
 * 游戏任务线程
 * @author Administrator
 *
 */
public class GameScheduleService {

	private NioEventLoop instance;
	private ExecutorService gameTableThreadPool;
	public void registerSchedule() throws InstantiationException, IllegalAccessException{
		instance = NioEventLoop.class.newInstance();		
		
		registerTableSchedule();  
	}
	
	
	//游戏守护线程池
	public void registerTableSchedule(){
		gameTableThreadPool = Executors.newSingleThreadExecutor(new NamedThreadFactory("table"));
		gameTableThreadPool.submit(new Runnable() {		
			@Override
			public void run() {
				while(true){
					
				}
			}
		});
	}	
	
	public void registerGameTaskSchedule(){
		instance.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
								
			}
			
		});
		
	}
	
}
