package com.common.log4j;

import java.io.FileNotFoundException;

import org.springframework.util.Log4jConfigurer;


/**
 * @author: tpp
 * @version: 2016年12月9日 下午1:04
 */
public abstract class Log4jGameConfigurer {
    
    private static final String DEFAULT_LOG4J_LOCATION = "src/main/java/com/game/config/log4j.xml";
    /**
     * 初始化 log4j
     */
    public static void initLogging() {
    	try {
			Log4jConfigurer.initLogging(DEFAULT_LOG4J_LOCATION);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * 关闭Log
     */
    public static void shutdownLogging() {
        try {
            Log4jConfigurer.shutdownLogging();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
