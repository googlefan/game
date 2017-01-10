package com.game.vo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.common.util.TimeTool;
import com.game.constant.Define;
import com.google.common.collect.Lists;

public class UserVo {

	private Long uid;
	private String userName;
	private String serverID;

	private String tableID;
	private boolean isActive = true;
	private long lastLeaveTime = TimeTool.snow();

	private Integer sex;
	private String icon;
	private int chip;
	private int subsidyCount;

	private List<Card> cardList = Lists.newArrayList();
	private int status = Define.UserStatusEnum.ONLINE.getValue();

	public String getUserName() {
		return userName;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServerID() {
		return serverID;
	}

	public void setServerID(String serverID) {
		this.serverID = serverID;
	}

	public String getTableID() {
		return tableID;
	}

	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getLastLeaveTime() {
		return lastLeaveTime;
	}

	public void setLastLeaveTime(long lastLeaveTime) {
		this.lastLeaveTime = lastLeaveTime;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getChip() {
		return chip;
	}

	public void setChip(int chip) {
		this.chip = chip;
	}

	public List<Card> getCardList() {
		return cardList;
	}

	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}

	public int getSubsidyCount() {
		return subsidyCount;
	}

	public void setSubsidyCount(int subsidyCount) {
		this.subsidyCount = subsidyCount;
	}

	public void addChip(int chip) {
		if (chip <= 0) {
			return;
		}
		this.chip += chip;
	}

	public void consumChip(int chip) {
		if (this.chip < chip) {
			this.chip = 0;
			return;
		}

		this.chip -= chip;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Card maxNetValueCard() {

		Collections.sort(this.cardList, new Comparator<Card>() {

			@Override
			public int compare(Card o1, Card o2) {
				// TODO Auto-generated method stub
				return o1.getNetValue() < o2.getNetValue() ? 1 : -1;
			}

		});

		return this.cardList.get(0);
	}
	
	public Card getCardById(String cardID) {
		for (Card card : this.cardList) {
			if (card.getCardID().equals(cardID)) {
				return card;
			}
		}

		return null;
	}
	
	

	// 重置用户数据
	public void resetUserData() {
		this.tableID = "";
	}

}
