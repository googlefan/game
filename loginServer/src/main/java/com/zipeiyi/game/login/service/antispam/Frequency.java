package com.zipeiyi.game.login.service.antispam;

/**
 * Created by zhangxiaoqiang on 16/12/5.
 */
public interface Frequency {

    public static final String FREQUENCY_LOGIN = "game_frequency_login_";

    public static final String FREQUENCY_SEND_SMS = "game_frequency_sms_";

    public static final int FREQUENCY_TIME_LIMIT = 60;

    public static final int MAX_LOGIN_TIMES = 5;

    public static final int FREQUENCY_SMS_TIME_LIMIT = 60;

    public static final int MAX_SMS_TIMES = 1;

    /**
     * 是否规定时间内超过次数限制
     *
     * @param key
     *            判断的唯一key
     * @param timesLimit
     *            限制次数
     * @param timeLimit
     *            限制时间，单位秒
     * @return 频率是否合法，true，正常，false，频率异常 FIXME 改成抛出异常的，FrequencyException改成FrequencyExceedException之类的
     */
    public boolean addTimes(String key, int timesLimit, int timeLimit);


    /**
     * 时间段内次数++
     *
     * @param key
     * @param timeLimit
     */
    public void addTimes(String key, int timeLimit);

    /**
     * 与addTimes 结合使用，判断key是否已经超出要求次数
     *
     * @param key
     * @param limit
     *            次数限制
     * @return
     */
    boolean isTimesOverFlow(String key, int limit);

    /**
     * 清空时间限制
     * @param key
     */
    public void clearTimeCache(String key);
}
