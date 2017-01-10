package com.common.nio.processor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.runtimeload.RunTimeLoader;
import com.common.util.ResourceUtils;

public class ProcessorLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessorLoader.class);
	
	Dispatcher dispatcher;
	String processorConfigLocation = "src/main/java/com/game/config/processors.properties";
	
	RunTimeLoader jruntimeLoader;
	
	/**
	 * 构造加载器
	 * @param jarsrc:原始processor.jar的存放位置
	 * @param runtimedir:JVM环境实际加载的jar位置
	 * @param processorsConfigFileInjar:processors.properties的位置
	 * @param msgDispatcher
	 * @throws IOException
	 */
	public ProcessorLoader(String srcJarPath, String runtimedir,
			String processorConfigLocation, Dispatcher dispatcher)
			throws IOException {
	    
	    logger.debug("初始化processor加载器环境, jarPath:{}, runtimedir:{}, processorConfigLocation:{}", srcJarPath, runtimedir, processorConfigLocation);
	    
		this.dispatcher = dispatcher;
		this.processorConfigLocation = processorConfigLocation;
		jruntimeLoader = new RunTimeLoader();
		try {
			jruntimeLoader.setRuntimeJarPath(runtimedir);
			jruntimeLoader.setSrcJarPath(srcJarPath);
			jruntimeLoader.reload();
		} catch (Exception e) {
			logger.error("使用了默认的加载器");
		}
	}
	
    public ProcessorLoader(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;	
	}
    
	@SuppressWarnings("unchecked")
	private void parseProcessor() throws IOException {
		Properties properties = new Properties();
//		System.out.println("111111"+this.getClass().getResource(processorConfigLocation)+processorConfigLocation);
//		InputStream inputStream = this.getClass().getResourceAsStream(processorConfigLocation);
		InputStream inputStream = new FileInputStream(ResourceUtils.getFile(processorConfigLocation)) ;
		
		properties.load(inputStream);
		// 覆盖原有的处理器

		Set<Entry<Object, Object>> es = properties.entrySet();
		Iterator<Entry<Object, Object>> it = es.iterator();
		Dispatcher sdispatcher = dispatcher;
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			final String key = (String) entry.getKey();
			final String value = (String) entry.getValue();

			try {
				String requestcode = key;
				String requestProcessor = value;
				int msgCode = Integer.valueOf(requestcode);
				Class<? extends Processor> processorClass;
				String[] str = requestProcessor.split(",");
				processorClass = (Class<? extends Processor>) this.getClass().getClassLoader().loadClass(str[0]);
				Object processorObj = processorClass.newInstance();
				Processor msgProcessor = Processor.class.cast(processorObj);
				msgProcessor.setCmd(msgCode);
				sdispatcher.addProcessor(msgCode, msgProcessor);
//
//				if (str.length >= 2) {
//					ProcessorConfig.getInstance().getProcessorInterval().put(msgCode, Long.parseLong(str[1]));
//					
//					if(str.length >= 3) {
//						ProcessorConfig.getInstance().getTooFastTime().put(msgCode, Integer.parseInt(str[2]));
//						
//						if(str.length >= 4) {
//							ProcessorConfig.getInstance().getOprations().put(msgCode, Integer.parseInt(str[3]));
//						}
//					}
//				}
			} catch (Exception e) {
				logger.error("构造" + value + "实例失败", e);
			}
		}
//		SessionMgr.setProcessors(dispatcher.getProcessors());
		
		logger.info("================================处理器加载完成================================");
		return;
	}

	
	public void load() throws IOException {
		// TODO Auto-generated method stub
		parseProcessor();
	}


}
