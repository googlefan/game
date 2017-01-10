package com.zipeiyi.game.login.utils;

import com.zipeiyi.game.common.util.RedisUtil;
import com.zipeiyi.game.login.WebConstants;
import com.zipeiyi.game.login.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录工具类
 *
 * @author zhangxiaoqiang
 * @createTime 2010-5-10
 */
public class LoginUtil {

    private static final String USER_SESSION_KEY = "login_user_session";

    private static Log logger = LogFactory.getLog(LoginUtil.class);

    protected LoginUtil() {
    }

    /**
     * 设置用户为已经登陆的状态
     *
     * @param request
     * @param user
     *            记住登陆
     */
    public static void setLogin(HttpServletRequest request, HttpServletResponse response,User user) {
        //token缓存，同时存储token到session key中
//        request.getSession().setAttribute(USER_SESSION_KEY, user);
        String uuid = getUUID();
        Cookie cookie = new Cookie("session_t", uuid);
        cookie.setDomain(WebConstants.DOMAIN);
        cookie.setMaxAge(-1);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        RedisUtil.getRedis().set(USER_SESSION_KEY + uuid,-1,user);
    }

    public static User getLoginSession(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String value = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_t")) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        User user = (User)RedisUtil.getRedis().get(USER_SESSION_KEY + value);
        return user;
    }

    /**
     * 清除登录
     *
     * @param request
     */
    public static void clearLogin(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_SESSION_KEY);

    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static float isMessyCode(String strName) {
        float result = 0;
        try {
            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();
            float chLength = ch.length;
            float count = 0;
            for (int i = 0; i < ch.length; i++) {
                char c = ch[i];
                if (!Character.isLetterOrDigit(c)) {

                    if (!isChinese(c)) {
                        count = count + 1;
                    }
                }
            }
            result = count / chLength;
        }
        catch (Exception ex) {

        }
        return result;

    }

    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
    }
}
