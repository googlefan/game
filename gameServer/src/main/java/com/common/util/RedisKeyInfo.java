/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.common.util;

/**
 *
 * @author Administrator
 */
public enum RedisKeyInfo {

    /*=============TYPE(prefix, ClassName|Key ,   Expire,   TableName,   isSyncToMysql)======================================*//*=============TYPE(prefix, ClassName|Key ,   Expire(单位：分钟),   TableName,   isSyncToMysql)======================================*/
    
    CHESS_USER_DATA("user", "ChessUserData", 0, "redis_chess_user_data", true),
    PLAYER_VO("user", "PlayerVo", 6 * 60, "", false),
    REAL_PRIZE_RECORD("user", "RealPrizeRecord", 2 * 60, "redis_key_map", true),
    USER_LOGIN_REWARD("user", "UserLoginReward", 2 * 60, "redis_key_map", true),
    USER_SERVER_DATA("user", "UserServerData", 30, "", false),
    PLAYER_QUEST("user", "PlayerQuest", 0, "redis_key_map", true),
    USER_ITEM("user", "UserItem", 0, "redis_user_item", false),
    USER_DAILY("user", "UserDaily", 24 * 60, "", false),
    USER_INVITION("", "Invitation", 0, "redis_Invitation", true),//邀请码
    PLAYER_DAILY_QUEST("user", "PlayerDailyQuest", 24 * 60, "", false),
    TABLE_VO("room", "TableVo", 6 * 60, "", false),
    TABLE_RESPONSE("", "table", 1, "", false),
    VIDEO("", "video", 7 * 24 * 60, "redis_video", true),
    COUNTER("user", "counter", 0, "redis_key_map", true),
    DAILY_COUNTER("user", "daily_counter", 24 * 60, "", false),
    FRIEND("", "friend", 24 * 60, "redis_key_map", true),
    CHAT_MESSAGE("", "message_chat", 24 * 60, "", false),
    EMAIL_MESSAGE("", "message_email", 24 * 60, "redis_key_map", true),
    NOTIC_MESSAGE("", "message_notic", 24 * 60, "", false),
    ACTIVITY_MESSAGE("", "message_activity", 24 * 60, "", false),
    FEED_BACK_MESSAGE("", "message_feedback", 24 * 60, "", false),
    REDIS_TO_MYSQL("", "sync_mysql", 0, "", false),
    ACTIVITY("", "activity", 24 * 60, "redis_activity", true),
    RANK("", "rank", 0, "redis_key_map", true),
    FRIEND_RANK("", "friend_rank", 24 * 60, "", false),
    CHAIRINFO("", "chairInfo", 60, "", false),
    MATCH("", "match", 0, "redis_match", true),
    MATCH_SIGN("match", "matchSign",0, "redis_match", true),
    MATCH_TAOTAI("match", "matchTaoTaiTemp", 7*24 * 60, "redis_match", false),
    MATCH_USER_SIGN("user", "matchSign", 0, "redis_match", true),
    MATCH_UPLEVEL("match", "uplevel", 7*24 * 60, "redis_match", true),
    MATCH_RANK("", "matchRank", 7*24*60, "redis_match", true),
    WEIXIN_TOKEN("", "weixin_token", 90, "", false),
    RECHARGE_DATA_WEIXIN("recharge", "weixin", 0, "redis_recharge_data", true),
    RECHARGE_DATA_ALIPAY("recharge", "alipay", 0, "redis_recharge_data", true),
    RECHARGE_DATA_LLPAY("recharge", "llpay", 0, "redis_recharge_data", true),
    MATCH_HISTORY("", "match_history", 0, "redis_match_history", true),
    RECHARGE_DATA_SENDTOUSER("recharge", "send", 0, "redis_recharge_send", true),
    ENTRY_ROOM_USER_NBUM("room", "number", 60, "", false),
    DEFAULT("", "default", 0, "", false),
    /**
     * 机器人队列
     */
    ROBOT("", "robots", 0, "", false),
    ROBOT_USING("", "robot_using", 0, "", false),
    TEMPRESPONSE("", "tempResponse", 1, "", false),
    /**
     * 玩家打开比赛推送列表
     */
    OPEN_MATCH_LIST("","openMatchList",0,"",false);

    private RedisKeyInfo(String prefix, String key, int minute, String table, boolean isSave) {
        this.prefix = prefix;
        this.key = key;
        this.minute = minute;
        this.table = table;
        this.isSave = isSave;
    }

    private String prefix;
    private String key;
    private int minute;
    private String table;
    private boolean isSave;

    public String getPrefix() {
        return prefix;
    }

    public String getKey() {
        return key;
    }

    public int getMinute() {
        return minute;
    }

    public String getTable() {
        return table;
    }

    public boolean isSave() {
        return isSave;
    }

    public static RedisKeyInfo getKeyInfo(String key) {
        for (RedisKeyInfo keyInfo : values()) {
            if (keyInfo.getKey().equalsIgnoreCase(key)) {
                return keyInfo;
            }
        }
        return null;
    }

}
