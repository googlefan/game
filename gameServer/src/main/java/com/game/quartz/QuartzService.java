package com.game.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: tpp
 * @version: 2016年12月15日 下午5:12:21
 */
public class QuartzService {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private  Scheduler scheduler = null;
    private JobDetail job = null;
    private Map<String, CronTrigger> triggerMap = new HashMap<String, CronTrigger>();
    
    private static QuartzService instance;
    
    private QuartzService() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            log.error("初始化quartz失败...", e);
        }
    }
    
    public static QuartzService getInstance(){
        if(instance == null){
            instance = new QuartzService();
        }
        return instance;
    }
    
    public void start(){
        if(scheduler != null){
            try {
                scheduler.start();
            } catch (SchedulerException e) {
                log.error("启动quartz失败...", e);
            }
        }
    }
    public void updateTriggerCronExpression(String jobName, String cronExpression) {
        CronTrigger trigger = triggerMap.get(jobName.trim());
        TriggerKey triggerKey = new TriggerKey(jobName + "Trigger",Scheduler.DEFAULT_GROUP);
        if (trigger != null) {
            try {
                scheduler.rescheduleJob(triggerKey, trigger);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    
    
    /**
     * 循环任务，到时间再触发
     *
     * @param clazz
     * @param dataMap
     * @param jobId
     * @param intervalTime 秒
     * @param startTime
     * @throws Exception
     */
    public void addCycleJobFromStartTime(Class<? extends Job> clazz, Map<String, Object> dataMap, String jobId, int intervalTime, Date startTime)
            throws Exception {
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobId)
                .build();
        if (dataMap != null) {
            jobDetail.getJobDataMap().putAll(dataMap);
        }
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .forJob(jobDetail)
                .startAt(startTime)
                .withIdentity(jobId)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(intervalTime))
                .build();
        
//        triggerMap.put(jobId, trigger);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 周期不变，修改触发时间
     *
     * @param jobId 触发器Id
     * @param startTime 触发时间 （毫秒）
     * @throws Exception
     */
    public  void modifyJobStart(String jobId, long startTime) {
        try {
            SimpleTriggerImpl triggerNew = (SimpleTriggerImpl) scheduler
                    .getTrigger(TriggerKey.triggerKey(jobId));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startTime);
            triggerNew.setStartTime(calendar.getTime());
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobId), triggerNew);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册一个定时任务
     * @param jobName
     * @param cronExpression
     * @param classs
     * @return
     */
    public void registerJob(String jobName, String cronExpression, Class<? extends Job> classs) {
        CronTrigger trigger = null;
        // 任务名称，任务组名称，任务实现类
        JobKey jobKey = new JobKey(jobName, Scheduler.DEFAULT_GROUP);
        job = JobBuilder.newJob(classs)
        .withIdentity(jobKey)
        .build();
        try {
            // 删除作业
            if (scheduler.getJobDetail(jobKey) != null) {
                scheduler.deleteJob(jobKey);
            }
            TriggerKey triggerKey = new TriggerKey(jobName + "Trigger",Scheduler.DEFAULT_GROUP);
            trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerKey)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();
           
            triggerMap.put(jobName, trigger);
            // 注册作业
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public  boolean checkExists(String jobId) {
        try {
            return scheduler.checkExists(new JobKey(jobId));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 修改触发间隔
     *
     * @param jobId 触发器Id
     * @param cycleTime 毫秒单位
     * @throws Exception
     */
    public void modifyJobInterval(String jobId, long cycleTime) throws Exception {
        try {
            SimpleTriggerImpl triggerNew = (SimpleTriggerImpl) scheduler
                    .getTrigger(TriggerKey.triggerKey(jobId));
            triggerNew.setStartTime(triggerNew.getNextFireTime());
            triggerNew.setRepeatInterval(cycleTime);
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobId), triggerNew);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
    
    public  boolean cancel(String jobId) {
        try {
            JobKey jk = new JobKey(jobId);
            if (!scheduler.checkExists(jk)) {
                return true;
            }
            return scheduler.deleteJob(jk);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void destroy() {
        try {
            if (scheduler != null)
                scheduler.shutdown();
        } catch (Exception e) {
            log.error("Quartz Scheduler failed to shutdown cleanly" , e);
        }
        log.info("Quartz Scheduler successful shutdown.");
    }

    public CronTrigger getTrigger(String key) {
        return triggerMap.get(key);
    }
}
