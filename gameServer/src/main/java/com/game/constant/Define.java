package com.game.constant;

/**
 * 
 * @author tpp
 *
 */
public class Define {

	//游戏类型 mysql redis
	public enum DBTypeDef{
		MYSQL(1),
		REDIS(2);
		
		private int dbType;
		private DBTypeDef(int dbType){
			this.dbType = dbType;
		}
		
		public int getValue(){
			return this.dbType;
		}
	}
	
	
//	//redis 数据类型
	public  enum RedisValueFormatDef{
		STRING(1),LIST(2),SET(3),HASH(4),ZSET(5);
		
		private int valueFormat;
		private RedisValueFormatDef(int valueFormat){
			this.valueFormat = valueFormat;
		}
		
		public int getValue(){
			return this.valueFormat;
		}
		
	};
//	
//	//操作类型  save get update delete
	public  enum DBOperateDef{
		SAVE(1),GET(2),UPDATE(3),DELETE(4);
		
		private int opType;
		private DBOperateDef(int opType){
			this.opType = opType;
		}
		
		public static DBOperateDef getDBOperateDefByValue(int val){
			for(DBOperateDef ob :DBOperateDef.values()){
				if(ob.getValue() == val){
					return ob;
				}
			}
			return null;
		}
		
		public int getValue(){
			return this.opType;
		}		
	};
	
	public enum GameOperatioinEnum{
		
		GRAB_BAKER(1),  //抢庄
		RAISE(2);		//下注
		
		private int opId;
		private GameOperatioinEnum(int opId){
			this.opId = opId;
		}
		
		public int getValue(){
			return opId;
		}
		
	}

	
	//////////////////////////////////////////////////////////////////////////
	//Table & Chair status
	/////////////////////////////////////////////////////////////////////////
	public enum GameStatusEnum{
		WAIT(0),
		FREE(1),
		CHOOSECARD_DONE(2),
		GRAB_BAKER(3),
		GRAB_BAKER_DONE(4),
		RAISE(5),
		RAISE_DONE(6),
		WATCHING(7),
		BALANCE(8),
		
		READY(9),
		EXIT(10)
		;
		
		private int status;
		private GameStatusEnum(int status){
			this.status = status;
		}
		
		public int getValue(){
			return status;
		}		
	}
	
	public enum UserStatusEnum{
		
		OFFLINE(0),
		ONLINE(1),
		LOADING(2);
		
		private int status;
		private UserStatusEnum(int status){
			this.status = status;
		}
		
		public int getValue(){
			return status;
		}		
	}
	
}
