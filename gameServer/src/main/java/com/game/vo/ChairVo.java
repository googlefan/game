package com.game.vo;

import java.util.ArrayList;
import java.util.List;

import com.common.util.TimeTool;
import com.game.constant.Define;
import com.google.common.collect.Lists;


public class ChairVo {

	private long uid;
	private int slot;
	private TableVo table;
	private int score;
	private int betScore;
	private List<Card> cardPool = Lists.newArrayList();	
	private List<Card> darkCardList = Lists.newArrayList();	
	private List<Card> openCardList = Lists.newArrayList();
	private int status;
	private long lastdoTime = TimeTool.snow();

	
	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public List<Card> getCardPool() {
		return cardPool;
	}

	public void setCardPool(List<Card> cardList) {
		this.cardPool = cardList;
	}

	public List<Card> getDarkCardList() {
		return darkCardList;
	}

	public void setDarkCardList(List<Card> darkCardList) {
		this.darkCardList = darkCardList;
	}

	public List<Card> getOpenCardList() {
		return openCardList;
	}

	public void setOpenCardList(List<Card> openCardList) {
		this.openCardList = openCardList;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	

	public long getLastdoTime() {
		return lastdoTime;
	}

	public void setLastdoTime(long lastdoTime) {
		this.lastdoTime = lastdoTime;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getBetScore() {
		return betScore;
	}

	public void setBetScore(int betScore) {
		this.betScore = betScore;
	}

	public Card getDarkCardById(String cardID) {

		for (Card card : cardPool) {
			if (card.getCardID() == cardID)
				return card;
		}
		return null;
	}

	public void chooseCard(Card openCard,Card darkCard){
		openCardList.add(openCard);
		darkCardList.add(darkCard);
		this.setStatus(Define.GameStatusEnum.CHOOSECARD_DONE.getValue());
		table.setTableOprateCount(1);
	}
	
	public void grabBanker(int score){
		this.score = score;
		this.setStatus(Define.GameStatusEnum.GRAB_BAKER_DONE.getValue());
		table.setTableOprateCount(1);
		
	}
	
	public void raise(int betScore){
		this.betScore = betScore;
		this.setStatus(Define.GameStatusEnum.RAISE_DONE.getValue());
		table.setTableOprateCount(1);		
	}
	
	public void confirmRestart(boolean isConfirm){
		
		if(isConfirm == true){
			this.setStatus(Define.GameStatusEnum.READY.getValue());
		}else{
			this.setStatus(Define.GameStatusEnum.EXIT.getValue());
		}
		table.setTableOprateCount(1);		
	}
	
	public void resetChairData(){
		this.score = 0;
		this.betScore = 0;
		this.cardPool.clear();
		this.darkCardList.clear();
		this.openCardList.clear();
		this.status = Define.GameStatusEnum.FREE.getValue();
		this.lastdoTime = TimeTool.snow();
	}
}
