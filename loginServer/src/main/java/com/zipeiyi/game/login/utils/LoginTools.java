package com.zipeiyi.game.login.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;

import java.util.Random;

public class LoginTools {

    private static int CHINESE_END = 40869;

    private static int CHINESE_START = 19968;

    private static int USER_NAME_LENGTH_LIMIT = 16;

    private static int USER_NAME_SHORT_LIMIT = 0;

    public static final int ERROR_EMAIL = 10;

    public static final int EMPTY_USER_NAME = 20;

    public static final int TOO_SHORT_TO_USER_NAME = 30;

    public static final int TOO_LONG_TO_USER_NAME = 40;

    public static final int CHAR_OVER_BOUND_TO_USER_NAME = 50;

    public static final int EMPTY_PASSWORD = 60;

    public static final int NOT_SAME_PASSWORD = 70;

    public static final int TOO_SHORT_TO_PASSWORD = 80;

    public static final int TOO_LONG_TO_PASSWORD = 100;

    public static final int ERROR_MOBILE_NUMBER = 90;

    public static final int CHAR_NOT_CORRECT_FOR_PASSWORD = 110;

    public static String getChineseErrorMsg(int code){
        switch (code) {
            case LoginTools.EMPTY_USER_NAME: {
                return "用户名不得为空";
            }
            case LoginTools.EMPTY_PASSWORD: {
                return "密码不能为空";
            }
            case LoginTools.NOT_SAME_PASSWORD: {
                return "两次输入密码不一致";
            }
            case LoginTools.TOO_SHORT_TO_PASSWORD: {
                return "密码为6-18位";
            }
            case LoginTools.TOO_LONG_TO_PASSWORD: {
                return "密码为6-18位";
            }
            case LoginTools.CHAR_NOT_CORRECT_FOR_PASSWORD:{
                return "密码至少包含一个英文字母和一个数字";
            }
            case LoginTools.ERROR_MOBILE_NUMBER:{
                return "用户名应该为正确手机号";
            }
            default:
                return "";
        }
    }

    public static int isInfoLegitimacy(String password, String repassword, String account) {

        int msg = chechPassword(password, repassword);
        if (msg > 0) {
            return msg;
        }
        if(StringUtils.isEmpty(account)){
            return EMPTY_USER_NAME;
        }
        msg = checkMobileNumber(account);
        return msg;
    }

    public static int checkUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return EMPTY_USER_NAME;
        }
        if (userName.length() < USER_NAME_SHORT_LIMIT) {
            return TOO_SHORT_TO_USER_NAME;
        }
        if (userName.length() > USER_NAME_LENGTH_LIMIT) {
            return TOO_LONG_TO_USER_NAME;
        }
        char[] nameArray = userName.toCharArray();
        for (char c : nameArray) {
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c=='-'|| (c >= CHINESE_START && c <= CHINESE_END))) {
                return CHAR_OVER_BOUND_TO_USER_NAME;
            }
        }
        return 0;
    }

    public static int chechPassword(String password, String repassword) {
        if (StringUtils.isBlank(password) || StringUtils.isBlank(repassword)) {
            return EMPTY_PASSWORD;
        }

        if (!password.equals(repassword)) {
            return NOT_SAME_PASSWORD;
        }

        if (password.length() < 6) {
            return TOO_SHORT_TO_PASSWORD;
        }

        if (password.length() > 18) {
            return TOO_LONG_TO_PASSWORD;
        }

        char[] nameArray = password.toCharArray();
        boolean isLCorrect = false;
        boolean isNCorrect = false;
        for (char c : nameArray) {
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                isLCorrect = true;
                break;
            }
        }
        for (char c : nameArray) {
            if ((c >= '0' && c <= '9')) {
                isNCorrect = true;
                break;
            }
        }
        if(!isLCorrect || !isNCorrect){
            return CHAR_NOT_CORRECT_FOR_PASSWORD;
        }

        return 0;
    }

    public static int checkMobileNumber(String mobile) {
        if (mobile.length() != 11) {
            return ERROR_MOBILE_NUMBER;
        }
        if (!mobile.startsWith("1")) {
            return ERROR_MOBILE_NUMBER;
        }
        for (int i = 0; i < mobile.length(); i++) {
            char c = mobile.charAt(i);
            if (c < '0' || c > '9') {
                return ERROR_MOBILE_NUMBER;
            }
        }
        return 0;
    }

    public static String mailURL(String email) {
        int pos = email.indexOf("@");
        String mailTail = email.substring(pos + 1);
        if (mailTail.startsWith("gmail")) {
            return "http://mail.google.com";
        }
        if(mailTail.startsWith("hotmail")){
            return "http://mail.live.com";
        }
        return "http://mail." + mailTail;
    }

    public static String mailServer(String email) {
        int pos = email.indexOf("@");
        int server = email.lastIndexOf(".");
        String mailTail = email.substring(pos + 1 , server);
        return mailTail;
    }

    private static final char[] CODE_ARRAY = { '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    public static String getRandString(int length) {
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(CODE_ARRAY[rand.nextInt(CODE_ARRAY.length)]);
        }
        return sb.toString();
    }
}
