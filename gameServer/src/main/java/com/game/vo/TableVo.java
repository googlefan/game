package com.game.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.util.TimeTool;
import com.common.util.Utils;
import com.game.constant.Define;
import com.game.quartz.GameQuartzTaskService;
import com.game.quartz.QuartzService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TableVo {

	private static final Logger logger = LoggerFactory.getLogger(TableVo.class);
	private String tableID;
	private String tableName;
	private long createTime = TimeTool.snow();

	private int bankerSlot = -1;
	private ChairVo[] chairs = new ChairVo[6];
	private int tableOprateCount = 0;

	private ConcurrentHashMap<Integer, Integer> lotteryPool = new ConcurrentHashMap<>(); // key:chairId,value:money

	private int status = Define.GameStatusEnum.FREE.getValue(); // table
	private long lastdoTime = TimeTool.snow();
	public boolean isActive = true;
	public boolean isGaming = false;

	public TableVo(String tableID) {
		this.tableID = tableID;
	}

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public ChairVo[] getChairs() {
		return chairs;
	}

	public void setChairs(ChairVo[] chairs) {
		this.chairs = chairs;
	}

	public ConcurrentHashMap<Integer, Integer> getLotteryPool() {
		return lotteryPool;
	}

	public void setLotteryPool(ConcurrentHashMap<Integer, Integer> lotteryPool) {
		this.lotteryPool = lotteryPool;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTableOprateCount() {
		return tableOprateCount;
	}

	public void setTableOprateCount(int tableOprateCount) {
		this.tableOprateCount += tableOprateCount;
	}

	public long getLastdoTime() {
		return lastdoTime;
	}

	public void setLastdoTime(long lastdoTime) {
		this.lastdoTime = lastdoTime;
	}

	public int getBankerSlot() {
		return bankerSlot;
	}

	public void setBankerSlot(int bankerSlot) {
		this.bankerSlot = bankerSlot;
	}

	//////////////////////////////////////////////////////
	public List<ChairVo> getUsedChairs() {

		List<ChairVo> usedList = Lists.newArrayList();
		for (ChairVo vo : chairs) {
			if (vo != null) {
				usedList.add(vo);
			}
		}
		return usedList;
	}

	/**
	 * 参与游戏的玩家
	 * 
	 * @return
	 */
	public List<ChairVo> getPlChairs() {
		List<ChairVo> plList = Lists.newArrayList();

		for (ChairVo vo : chairs) {
			if (vo != null && vo.getStatus() != Define.GameStatusEnum.WATCHING.getValue()) {
				plList.add(vo);
			}
		}
		return plList;
	}

	public Map<Integer, ChairVo> getNotReadyChairs() {
		Map<Integer, ChairVo> notReadyMap = Maps.newHashMap();

		for (ChairVo vo : chairs) {
			if (vo != null) {
				if (vo.getStatus() == Define.GameStatusEnum.EXIT.getValue()) {
					notReadyMap.put(vo.getSlot(), vo);
				}
			}

		}
		return notReadyMap;
	}

	public boolean checkUserIsInTable(long uid) {

		for (ChairVo vo : chairs) {
			if (vo != null && vo.getUid() == uid) {
				return true;
			}
		}
		return false;
	}

	public boolean isFull() {

		if (this.getUsedChairs().size() >= this.chairs.length)
			return true;

		return false;
	}

	public boolean isCompletCurStage() {
		List<ChairVo> usedList = this.getUsedChairs();

		int zcount = 0;
		for (ChairVo ch : usedList) {
			if (ch.getStatus() != Define.GameStatusEnum.WATCHING.getValue()) {
				zcount++;
			}
		}
		if (this.tableOprateCount >= zcount)
			return true;

		return false;
	}

	public int changeGameStatus(int status) {

		List<ChairVo> plList = this.getPlChairs();
		for (ChairVo chair : plList) {
			chair.setStatus(status);
		}
		return -1;
	}

	public ChairVo getChairByUid(long uid) {

		for (ChairVo vo : chairs) {
			if (vo != null && vo.getUid() == uid) {
				return vo;
			}
		}
		return null;
	}

	public int jion(long uid) {

		ChairVo vo = getChairByUid(uid);
		if (vo == null && this.getUsedChairs().size() < this.chairs.length) {
			for (int i = 0; i < chairs.length; i++) {
				vo = chairs[i];
				if (vo == null) {
					vo = new ChairVo();
					vo.setSlot(i);
					vo.setUid(uid);
					chairs[i] = vo;
					return i;
				}
			}
		}
		if (vo != null)
			return vo.getSlot();

		return -1;
	}
	
	// 计算桌子的匹配权重
	public float caculateTableWeight() {
		int size = this.getUsedChairs().size();
		if(size < this.chairs.length){
			
		}
		return 1;
	}

	public void resetTableData() {

		List<ChairVo> usedList = this.getUsedChairs();
		this.status = Define.GameStatusEnum.FREE.getValue();
		this.isActive = true;
		this.isGaming = false;
		this.bankerSlot = -1;
		this.lastdoTime = TimeTool.snow();
		this.tableOprateCount = 0;

		for (ChairVo chair : usedList) {
			chair.resetChairData();
		}
	}

	private static String generatorTableID(long now) {
		String str = Utils.getRandomString(5);
		return now + str;
	}

	// 人数条件满足 启动桌子定时任务
	public void startTableJob() throws Exception {
		
		Map<String, Object> dataMap = Maps.newHashMap();
		dataMap.put("tableID", tableID);

		logger.info("create Table task start:  talbeID:" + tableID);

		GameQuartzTaskService.getInstance().registerRunTableJob(dataMap, tableID, 5, TimeTool.getDate());
	}

	public static TableVo createTable() throws Exception {
		long nowTime = TimeTool.snow();
		String tableID = generatorTableID(nowTime);
		TableVo table = new TableVo(tableID);
		table.startTableJob();
		return table;
	}

	public void modifyTableJob(Date targeTime) {
		try {
			logger.warn("-----------------------------------------------------------修改守护线程" + this.getTableID() + "--"
					+ targeTime.getTime());
			// String tableJob = JobId.tableId + this.getTableID();
			if (QuartzService.getInstance().checkExists(this.getTableID())) {
				QuartzService.getInstance().modifyJobStart(this.getTableID(), targeTime.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 立即执行守护线程
	 */
	public void doTableJobNow() {
		try {
			// String tableJob = JobId.tableId + this.getTableid();
			if (QuartzService.getInstance().checkExists(this.getTableID())) {
				QuartzService.getInstance().modifyJobStart(this.getTableID(), new Date().getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除守护线程
	 */
	public static void cancelTableJob(String tableID) {
		try {
			logger.warn("-----------------------------------------------------------删除守护线程" + tableID);
			// String tableJob = this.getTableID();
			if (QuartzService.getInstance().checkExists(tableID)) {
				QuartzService.getInstance().cancel(tableID);// (tableJob);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
