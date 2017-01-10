package com.game.db.redis.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.util.RedisKeyUtil;
import com.game.db.redis.RedisDao;
import com.game.db.redis.RedisService;
import com.game.vo.ChairVo;
import com.game.vo.TableVo;
import com.game.vo.UserVo;
import com.google.common.collect.Maps;
import com.zipeiyi.core.common.utils.JSONUtil;
import com.zipeiyi.game.common.util.JsonUtil;
import com.zipeiyi.game.common.util.StringUtil;
import com.zipeiyi.xpower.cache.IRedis;

public class RedisDaoImp implements RedisDao {

	private static final Logger logger = LoggerFactory.getLogger(RedisDaoImp.class);
	private static RedisDaoImp instance;
	IRedis redisService = RedisService.getInstance().getRedis();

	public static RedisDaoImp getInstance() {
		if (instance == null) {
			instance = new RedisDaoImp();
		}
		return instance;
	}

	@Override
	public TableVo getTableVo(String tableID) {

		// TODO Auto-generated method stub
		String tableStr = redisService.hget(RedisKeyUtil.getRoomKey(1, TableVo.class), tableID);
		return (TableVo) JsonUtil.getBeanFromJson2(tableStr, TableVo.class);
	}

	@Override
	public TableVo getNotFullTable() {
		// TODO Auto-generated method stub
		String tableID = getNoFullTableId();
		if (!StringUtil.isEmpty(tableID)) {
			String json = redisService.hget(RedisKeyUtil.getRoomKey(1, TableVo.class), tableID);
			return (TableVo) JsonUtil.getBeanFromJson2(json, TableVo.class);
		}
		return null;
	}

	public String getNoFullTableId() {
		// String tKey = getNotFullTableKey(tableID);
		Map<String, Double> map = redisService.zrevrangeWithScore(RedisKeyUtil.getNotFullTableKey(1, TableVo.class), 0,
				1);
		if (map.isEmpty()) {
			return null;
		}
		String tableStr = null;
		for (String keys : map.keySet()) {
			if (map.get(keys) <= 0) {
				return null;
			}
			tableStr = keys;
		}
		return tableStr;
	}
	
	
	public Map<String, Double> getNoFullTableIds() {
		// String tKey = getNotFullTableKey(tableID);
		Map<String, Double> map = redisService.zrevrangeWithScore(RedisKeyUtil.getNotFullTableKey(1, TableVo.class), 0,
				1);
		if (map.isEmpty()) {
			return Maps.newHashMap();
		}
		return map;
	}

	@Override
	public UserVo getUserVoById(long uid) {
		// TODO Auto-generated method stub
//		String key = RedisKeyUtil.getUserKey(uid, UserVo.class);
		String json = redisService.getString(RedisKeyUtil.getUserKey(uid, UserVo.class));
		UserVo uvo = (UserVo) JsonUtil.getBeanFromJson(json, UserVo.class);
		return uvo;
	}

	@Override
	public void saveTableVo(TableVo tableVo) {
		// TODO Auto-generated method stub
		String json = JsonUtil.getJsonFromBean(tableVo);
		redisService.hset(RedisKeyUtil.getRoomKey(1, TableVo.class), tableVo.getTableID(), json);
		updateNotFullTable(tableVo.getTableID(), tableVo.caculateTableWeight());
	}

	private void updateNotFullTable(String tableID, float weight) {
		String key = RedisKeyUtil.getNotFullTableKey(1, TableVo.class);
		redisService.zadd(key, weight, tableID);
	}

	private void delNotFullTable(String tableID) {
		String key = RedisKeyUtil.getNotFullTableKey(1, TableVo.class);
		redisService.zrem(key, tableID);
	}

	@Override
	public void delTableVo(String tableID) {
		// TODO Auto-generated method stub
		redisService.hdel(RedisKeyUtil.getRoomKey(1, TableVo.class), tableID);
		delNotFullTable(tableID);
	}

	@Override
	public ChairVo getChairVo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveChairVo(ChairVo chairVo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delChairVo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveUserVo(UserVo user) {
		// TODO Auto-generated method stub

		System.out.println("sent to redis ======"+JsonUtil.getJsonFromBean(user));
		boolean isSuccess = redisService.setString(RedisKeyUtil.getUserKey(user.getUid(), UserVo.class),
				JsonUtil.getJsonFromBean(user));
		if (!isSuccess) {
			logger.error("saveUserVo error !!! uid::" + user.getUid());
		}
	}

	@Override
	public void delUserVo(long uid) {
		// TODO Auto-generated method stub
		redisService.delete(RedisKeyUtil.getUserKey(uid, UserVo.class));
	}
	// public static void main(String args[]){
	// TableVo table = new TableVo("1234567");
	// table.jion(33333);
	//
	// RedisDaoImp ttt = new RedisDaoImp();
	// ttt.saveTableVo(table);
	// }

	@Override
	public void getAllTableVos() {
		 Map<String, String> map = redisService.hgetAll(RedisKeyUtil.getRoomKey(1, TableVo.class));
		 for(String key :map.keySet()){
			 logger.info("delKey=============="+key);
			 this.delTableVo(key);
		 }
//		return (TableVo) JsonUtil.getBeanFromJson2(tableStr, TableVo.class);
	}

}
