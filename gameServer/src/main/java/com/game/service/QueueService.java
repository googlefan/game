package com.game.service;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.vo.UserVo;

public class QueueService {

	private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
	private ConcurrentHashMap<Integer, UserVo> queue = new ConcurrentHashMap<Integer, UserVo>() ;
	
	public void addUserToQueue(UserVo uvo){
		if(queue.get(uvo.getUid())!=null){
//			logger.info(String.format("", args));
		}
	}
}
