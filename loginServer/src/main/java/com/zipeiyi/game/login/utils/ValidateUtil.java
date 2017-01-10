package com.zipeiyi.game.login.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author zhangxiaoqiang
 * @createTime 2012-6-19
 */
public class ValidateUtil {

    public static boolean validateMoblie(String phone) {
        if (phone == null) {
            return false;
        }
        int l = phone.length();
        boolean rs = false;
        switch (l) {
            /*
        case 7:
            if (matchingText("^(1[1-9][0-9])\\d{4}$", phone)) {
                rs = true;
            }
            break;       */
        case 11:
            if (matchingText("^(1[1-9][0-9])\\d{4,8}$", phone)) {
                rs = true;
            }
            break;
        default:
            rs = false;
            break;
        }
        return rs;
    }

    private static boolean matchingText(String expression, String text) {
        Pattern p = Pattern.compile(expression); // 正则表达式
        Matcher m = p.matcher(text); // 操作的字符串
        boolean b = m.matches();
        return b;
    }

    /**
     * 是否含有屏蔽词
     *
     * @param
     * @return
     */
//    public static boolean hasFobiddenWords(long userId, String content, String ip) {
////        CheckResult cr = AntiSpamAdapter.getInstance().antispamFilter(0, 0, CheckType.NUOMI_GOSSIP, content, ip);
////        return cr.getFlag() != CheckResult.SAFE;
//        return true;
//    }

    public static boolean validateEmail(String arg) {
        if (arg == null) return false;
        arg = arg.trim();
        String regEmail = "([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}";
       return matchingText(regEmail,arg);
    }

    /**验证电话号码==========。*/
    public static boolean validatePhone(String arg) {
        if (arg == null) return false;
        arg = arg.trim();
        String regPhone = "(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?";
        return matchingText(regPhone,arg);
    }

    /**验证手机号码-----*/
    public static boolean validateMobilePhone(String arg) {
        if (arg == null) return false;
        arg = arg.trim();
        String regMobile = "1[3|4|5|6|7|8|9][0-9]{1}[0-9]{8}";
        return matchingText(regMobile,arg);
    }

    /**验证QQ号*/
    public static boolean validateQQ(String arg) {
        if (arg == null) return false;
        arg = arg.trim();
        String regQQ ="[1-9]\\d{4,9}";
        return matchingText(regQQ,arg);
    }

    /**验证IP*/
    public static boolean  validateLength(String arg,int min,int max) {
        if (arg == null) return false;
        arg = arg.trim();
        if (arg.length() < min || arg.length() > max) {
            return false;
        }else{
            return true;
        }
    }

    /**验证网址============以http://开头*/
    public static boolean  validateNet(String arg) {
        if (arg == null) return false;
        arg = arg.trim();
        String regNet = "http:\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- .\\/?%&=]*)?";
        return matchingText(regNet,arg);
    }

    /**验证IP*/
    public static boolean  validateIP(String arg) {
        if (arg == null) return false;
        arg = arg.trim();
        String checkIp = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])";
        return matchingText(checkIp,arg);
    }

}
