/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.common.util;

import com.zipeiyi.game.common.util.StringUtil;

/**
 * 创建redis规范key
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class RedisKeyUtil {

    private static final String USER = "user";
    private static final String ROOM = "room";
    private static final String NOT_FULL_TABLE = "notFullTalbe";

    public static final String F = ".";
    private static final String GAMELOADCONFIG = "gameloadconfig";
    private static final String FILTER_WORDS = "filter_words";

    /**
     * redis HashMap</br>
     * key:BaseType(String ,int...)</br>
     * 用户表
     *
     * @param userid
     * @param obj
     * @return
     */
    public static String getUserKey(Object userid, Class obj) {
        StringBuilder key = new StringBuilder();
        key.append(USER);
        key.append(F);
        key.append(userid);
        key.append(F);
        key.append(obj.getSimpleName());
        return key.toString();
    }

    /**
     * redis HashMap</br>
     * key:BaseType(String ,int...)</br>
     * 加载码表
     *
     * @param userid
     * @param obj
     * @return
     */
    public static String getGameLoadConfigKey(Class obj) {
        StringBuilder key = new StringBuilder();
        key.append(GAMELOADCONFIG);
        key.append(F);
        key.append(obj.getSimpleName());
        return key.toString();
    }

    /**
     * redis HashMap</br>
     * key:BaseType(String ,int...)</br>
     * 用户表
     *
     * @param roomid
     * @return
     */
    public static String getRoomKey(Object roomid, Class clas) {
        StringBuilder key = new StringBuilder();
        key.append(ROOM);
        key.append(F);
        key.append(roomid);
        key.append(F);
        key.append(clas.getSimpleName());
        return key.toString();
    }
    
    public static String getNotFullTableKey(Object roomid, Class clas) {
        StringBuilder key = new StringBuilder();
        key.append(NOT_FULL_TABLE);
        key.append(F);
        key.append(roomid);
        key.append(F);
        key.append(clas.getSimpleName());
        return key.toString();
    }
    
    public static String getFilterWordsKey() {
        return FILTER_WORDS;
    }

    /**
     * prefix.param1.param2.key
     * @param keyInfo
     * @param params
     * @return 
     */
    public static String getClassTypeRedisKey(RedisKeyInfo keyInfo,String... params){
        StringBuffer sb = new StringBuffer();
        String prefix = keyInfo.getPrefix();
        if(StringUtil.isNotNull(prefix)){
            sb.append(prefix).append(F);
        }
        for (String param : params) {
            sb.append(param).append(F);
        }
        String key = keyInfo.getKey();
        sb.append(key);
        return sb.toString();
    }
    
    /**
     * prefix.key.param1.param2.key
     * @param keyInfo
     * @param params
     * @return 
     */
    public static String getCommonTypeRedisKey(RedisKeyInfo keyInfo,String... params){
        StringBuffer sb = new StringBuffer();
        String prefix = keyInfo.getPrefix();
        if(StringUtil.isNotNull(prefix)){
            sb.append(prefix).append(F);
        }
        String key = keyInfo.getKey();
        sb.append(key);
        sb.append(F);
        for (String param : params) {
            sb.append(param).append(F);
        }
        sb.append(key);
        return sb.toString();
    }
    
    public static RedisKeyInfo getRedisKeyInfo(String key){
        for (RedisKeyInfo info : RedisKeyInfo.values()) {
            if(key.equalsIgnoreCase(info.getKey())){
                return info;
            }
        }
        return RedisKeyInfo.DEFAULT;
    }
    
    public static String getInfoKeyStr(String key){
        if(key.isEmpty())
            return "";
        return key.substring(key.lastIndexOf(F)+1);
    }
     public static String getGameMatchUpLevelKey(String key) {
        return "matchUplevel-"+key;
    }
     
    public static String getGameMatchVideosKey(String matchNoVideo) {
        return "matchVideo:"+matchNoVideo;
    }
    public static enum OPERATE{
        string(1),
        hash(2),
        list(3),
        set(4),
        zset(5),
        ;

        private OPERATE(int type) {
            this.type =type;
        }
        
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
        
    }
}
