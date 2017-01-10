package com.zipeiyi.game.common.util;

/**
 * Created by Administrator on 2016/12/27.
 */
public class CmdSignUtil {

    //gateserver与db server通信使用
    private static final String SALT = "CheT3sdgdsa5OesAhpha7soVoGhallsjsxxxxf2Odiehe7EiPhu7ahde";

    private static final String CMD_SALT = "CheT3saws1@a5OesAha7soVoGhQGws#ss!powf2Odiehe7EiPhu7ahde";

    public static String getUserInfoSign(long uid,long timestamp){
        String hash1 = MD5Util.getMD5ofStr(uid+"");
        return MD5Util.getMD5ofStr(hash1 + SALT + timestamp);
    }

    public static String getCmdSign(long uid,int cmd,long time,String ticket){
        return MD5Util.getMD5ofStr(""+cmd + ticket + time + CMD_SALT + uid);
    }
}
