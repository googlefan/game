package com.zipeiyi.game.common;

public class ModuleCmd {
	
	public enum moduleCmd{

		EncryptCheck(1001),
		MainGame(1002),
		GameDirect(1003),

		GameOffline(2001),
		ServerBeat(2002),
		ClientBeat(2003),

		GameJoin(5001), 
		GameChooseCard(5002),
		GameGrabBanker(5003), 
		GameRaise(5004),
		GameReStartConfirm(5005),
		GameUseSubsidy(5006),
		GameExit(5007), 

		
		GameCards(5101),
		GameAutoChooseCard(5102),
		GameTable(5103),
		GameBankerRet(5104),
		GameRaiseRet(5105),
		GameBalance(5106),
		GameEnterTable(5107),
		GameExitTable(5108),
		GateUserOffLine(5109),
		
		
//		////////////////////////////////////////DB//////////////////////////////
//		
		DBHallUserInfo(9001),
		DBUerGuidDone(9002), //用户属性更新接口
		
		DBUserInfo(9100), 
		DBCardList(9102),
		DBGameBalance(9103);
		
		int value;

		private moduleCmd(int val) {
			value = val;
		}
		
		public static int getValueByName(String name){
			 for(ModuleCmd.moduleCmd module :ModuleCmd.moduleCmd.values()){
				 if(module.toString().equals(name)){
					 return module.value;
				 }
			 }
			 return -1;
		}
	}
	/**
	 * 加入游戏队列
	 */
	public static final int joinGame = 5001;
	/**
	 * 选牌
	 */
	public static final int chooseCard = 5002;
	/**
	 * 抢庄
	 */
	public static final int grabBanker = 5003;
	/**
	 * 下注
	 */
	public static final int raise = 5004;
	/**
	 * 重新开始确认
	 */
	public static final int restartConfirm = 5005;	
	/**
	 * 使用破产补助
	 */
	public static final int useSubsidy = 5006;		
	
	/**
	 * 逃跑
	 */
	public static final int escape = 5007;		
	
	
	/**
	 * 选牌界面推送
	 */
	public static final int cardListPush = 5101;
	/**
	 * 系统选牌
	 */
	public static final int autoChooseCard = 5102;
	/**
	 * 游戏组推送
	 */
	public static final int tablePush = 5103;
	/**
	 * 庄家结果推送
	 */
	public static final int bankerRetPush = 5104;
	/**
	 * 下注
	 */
	public static final int raisePush = 5105;
	/**
	 * 结算
	 */
	public static final int balancePush = 5106;
	/**
	 * 加入游戏组
	 */
	public static final int enterTablePush = 5107;
	/**
	 * 退出游戏组
	 */
	public static final int exitTablePush = 5108;
	
	
	////////////////////////////////////////DB//////////////////////////////
	
	public static final int center_userByID = 9001;
	public static final int center_cardList = 9002;
}
