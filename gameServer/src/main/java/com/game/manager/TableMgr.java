package com.game.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableMgr {
	private static final Logger logger = LoggerFactory.getLogger(TableMgr.class);

	private static TableMgr instance = new TableMgr();
	public static TableMgr getInstance(){
		return instance;
	}
	
	public void Init(){
		
	}
	
	
}
