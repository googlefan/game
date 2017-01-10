package com.game.quartz;

import java.util.Date;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.Job;

/**
 * 服务定时任务处理器
 * 
 * @author tpp
 * 
 */
public class GameQuartzTaskService {

	private final QuartzService quartzService;
	private static GameQuartzTaskService instance;
	 
	public static GameQuartzTaskService getInstance(){		
		return instance;
	}
	
	public void Init(){
		instance = this;
	}
	

	public GameQuartzTaskService(QuartzService quartzService) {

		this.quartzService = quartzService;
		
		Init();
	}

	/**
	 * 注册任务
	 */
	public void registerTask() {

	}

	/**
	 * 用来测试Quartz的,你写的每个任务,都可以在这里先测试通过
	 * 
	 * 测试的时候打开 register()方法里打开测试服务, 测试完了,关闭掉----不要提交打开的
	 */
	private void registerTest() {
		registerJob("test", "0 0 1-23 * * ?", TestQuartz.class);
	}

	/**
	 * table 定时执行到时操作
	 */
	public void registerRunTableJob(String refreshTime) {
		registerJob("TableQuartz", refreshTime, TableQuartz.class);
	}

	public void registerRunTableJob(Map<String, Object> dataMap, String jobId, int intervalTime, Date startTime)
			throws Exception {
		registerJob(TableQuartz.class, dataMap, jobId, intervalTime, startTime);
	}

	private void registerJob(String jobName, String cronExpression, Class<? extends Job> classs) {
		this.quartzService.registerJob(jobName, cronExpression, classs);
	}

	private void registerJob(Class<? extends Job> classs, Map<String, Object> dataMap, String jobId, int intervalTime,
			Date startTime) throws Exception {
		this.quartzService.addCycleJobFromStartTime(classs, dataMap, jobId, intervalTime, startTime);
	}

	public CronTrigger getTrigger(String key) {
		return quartzService.getTrigger(key);
	}

}
