package com.game.quartz;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.module.TexasHoldemServerImp;
//import com.game.service.GameService;

public class TableQuartz implements Job {

	private static final Logger logger = LoggerFactory.getLogger(TableQuartz.class);
	static ExecutorService threadPools = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		threadPools.execute(new Runnable() {		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, Object> data = (Map)context.getJobDetail().getJobDataMap();
				TexasHoldemServerImp.getInstance().runTableJob(data);
			}
		});		
	}

}
