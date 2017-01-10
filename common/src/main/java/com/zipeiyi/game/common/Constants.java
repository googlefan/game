package com.zipeiyi.game.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量池
 * Created by zhangxiaoqiang on 16/12/5.
 */
public class Constants {
    /////////////////////////////////心跳指令码//////////////////////////////
    public static final int OFFLINE_NOTIFY = 2001; //gate心跳监测，掉线通知game server

    public static final int SERVER_BEAT_COMMOND = 2002;  //服务器端心跳包

    public static final int CLIENT_BEAT_COMMOND = 2003; //客户端心跳包


    /////////////////////////////////gate server与db server交互指令码//////////////////////////////
    public static final int REQUEST_TOKEN_VERIFY = 1001; //请求指令验证

    public static final int FETCH_GAME_ROLE_INFO = 1002;//进入大厅获取角色相关用户信息

    public static final int USER_GAME_DIRECT_NOTIFY = 1003; //新手引导完成


    
    /////////////////////////////////Game Start//////////////////////////////
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
	 * 用户主动逃跑
	 */
	public static final int gameEscape = 5007;

	/**
	 * 通用加密验证
	 */
	public static final int commonCrypty = 9000;

	/**
	 *大厅获取用户数据
	 */
	public static final int mainUserInfo = 9001;

	/**
	 * 新手引导
	 */
	 public static final int gameUserInfoGuide = 9002;

	/**
	 *游戏中获取用户信息
	 */
	public static final int gameUserInfo = 9100;

	/**
	 *游戏暗牌
	 */
	public static final int gameDarkCard = 9102;

	/**
	 *游戏结算信息
	 */
	public static final int gameResult = 9103;


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
	
	public static final int center_userByID = 6001;
	public static final int center_cardList = 6002;

	////////////////////////////////////////Game End//////////////////////////////

	
    
    /**指令请求返回状态码**/
    public static final int SUCCESS_CODE = 1; //成功

    public static final int PARAM_ERROR_CODE = -1; //参数错误

    public static final int FREQUENCY_ERROR_CODE = -2;//频率错误

    public static final int ERROR_COMMAND = -3; //指令码错误

    public static final int TOKEN_VERIFY_ERROR_CODE = -4; //token验证错误


    /**
     * 指令返回的msg信息
     */
    public static final String SUCCESS_MSG = "SUCCESS";

    public static final String PARAM_ERROR_MSG = "数据请求异常";

    public static final String TOKEN_VERIFY_ERROR = "请求验证错误，无效请求";

    public static final List<Integer> COMMONIDS = new ArrayList<>();

    static {
        COMMONIDS.add(SERVER_BEAT_COMMOND);
        COMMONIDS.add(CLIENT_BEAT_COMMOND);
        COMMONIDS.add(FETCH_GAME_ROLE_INFO);
        COMMONIDS.add(USER_GAME_DIRECT_NOTIFY);

        COMMONIDS.add(joinGame);
        COMMONIDS.add(chooseCard);
        COMMONIDS.add(grabBanker);
        COMMONIDS.add(raise);
        COMMONIDS.add(restartConfirm);
        COMMONIDS.add(useSubsidy);
        COMMONIDS.add(grabBanker);

		COMMONIDS.add(gameEscape);
		COMMONIDS.add(mainUserInfo);
		COMMONIDS.add(gameUserInfo);
		COMMONIDS.add(gameDarkCard);
		COMMONIDS.add(gameResult);

	}

    /**
     * 用户所在game server的定位，redis key = key + uid，返回数据结构见com.zipeiyi.game.common.model.UserLocation
     */
    public static final String USER_GAME_SERVER_LOCATION_KEY = "user_game_server_location_";

    /**
     * 用户所在gate server的定位，用于断线重新，掉线，刷新，重新进入逻辑，redis key = key + uid,返回数据结构见com.zipeiyi.game.common.model.UserGateLocation
     */
    public static final String USER_GATE_SERVER_LOCATION_KEY = "user_gate_server_location_";

    /**
     * 保存用户基本信息key,在新插入，更改属性时候，需要移除该key,key = key + uid
     */
    public static final String USER_BASIC_INFO = "user_login_id_basic_info_";

    public static final String USER_ACCOUNT_BASIC_INFO = "user_login_account_basic_info_";

    /**
     * *******************Foramt Use Constants****************************
     */
    public static final String formatYmd = "yyyyMMdd";
}
