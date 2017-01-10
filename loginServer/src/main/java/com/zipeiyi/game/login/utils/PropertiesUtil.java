package com.zipeiyi.game.login.utils;


import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName: PropertiesUtil
 * @Description:解读属性配置文件
 * @author: dongyejun
 * @date: 2016年8月12日 上午10:47:16
 */
public class PropertiesUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PropertiesUtil.class);

	/**
	 * 
	 */
	public static Map<String,String> getAllProperty(String fileName){
		String path = Thread.currentThread().getContextClassLoader().getResource(fileName).getPath();
		Properties prop = new Properties();
		try {
			InputStream is = new FileInputStream(path);
			prop.load(is);
		}catch (FileNotFoundException e) {
			logger.error("getAllProperty(String,)", e);

		}catch (IOException e) {
			logger.error("getAllProperty(String)", e);
		}

		Enumeration<?> enumeration = prop.propertyNames();//得到配置文件的名字
		Map<String,String> map=Maps.newHashMap();
		while(enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			String value = prop.getProperty(key);
			map.put(key, value);
		}
		return map;
	}
	/**
	 * 
	 * @Title: getProperty
	 * @Description: 如果没有，则返回字符串
	 * @param path
	 * @param key
	 * @return
	 * @return: String
	 */
	public static String getProperty(String fileName, String key) {

		String path = Thread.currentThread().getContextClassLoader().getResource(fileName).getPath();
		File file = new File(path);
		if (!file.exists()) {
			// 创建文件夹
			file.mkdirs();
			// 创建文件

			FileOutputStream fos;
			try {
				fos = new FileOutputStream(path);
				fos.close();
			}catch (FileNotFoundException e) {
				logger.error("getProperty(String, String)", e);
			}catch (IOException e) {
				logger.error("getProperty(String, String)", e);
			}
			return "";

		}else {
			Properties prop = new Properties();
			try {

				InputStream is = new FileInputStream(path);

				prop.load(is);
			}catch (FileNotFoundException e) {
				logger.error("getProperty(String, String)", e);
				e.printStackTrace();

			}catch (IOException e) {
				logger.error("getProperty(String, String)", e);
				e.printStackTrace();
			}

			return prop.getProperty(key);
		}

	}
	
	/**
	 * 设置属性文件
	 * @param path
	 * @param key
	 * @param value
	 */
	public static void setProper(String path, String key, String value) {

		String resourceFile = path;

		Properties prop = new Properties();
		try {
			if (resourceFile.indexOf(".properties") == -1) {
				resourceFile += ".properties";
			}
			FileInputStream fis = new FileInputStream(resourceFile);
			try {
				prop.load(fis);
				fis.close();
				prop.setProperty(key, value);
				FileOutputStream fos = new FileOutputStream(resourceFile);
				prop.store(fos, "Copyright Thcic");
				fos.close();
			}catch (IOException e) {
				logger.error("setProper(String, String, String)", e);
				logger.error("修改资源文件：" + resourceFile + "异常！msg：" + e.getMessage());
			}

		}catch (FileNotFoundException e) {
			logger.error("setProper(String, String, String)", e);
			logger.error("无法获得资源文件：" + resourceFile);
		}

	}

}
