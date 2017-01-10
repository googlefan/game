/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zipeiyi.game.common.util;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * 字符串基本操作
 *
 * @author lxf
 */
public class StringUtil {

    public static final char COMMAND_NAME_PAD_CHAR = ' ';

    /**
     * 指定长度得字符串，空格补全
     *
     * @param str
     * @param length
     * @return
     */
    public static String getBuild_str(String str, int length) {
        StringBuilder strb = new StringBuilder(str);
        int df = length - str.length();
        for (int i = 0; i < df; i++) {
            strb.append(COMMAND_NAME_PAD_CHAR);
        }
        return strb.toString();
    }

    /**
     * 将字符串转化为指定长度的字符数组，空格补全
     *
     * @param str
     * @param length
     * @return
     */
    public static byte[] getBuild_byte(String str, int length) {
        if (str != null && !"".equals(str) && !str.equals("null")) {
            try {
                StringBuilder strb = new StringBuilder();

                byte[] str_byte = str.getBytes("UTF-8");
                strb.append(str);
                int df = length - str_byte.length;
                for (int i = 0; i < df; i++) {
                    strb.append(COMMAND_NAME_PAD_CHAR);
                }
                return strb.toString().getBytes("UTF-8");
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 字节数组截取
     *
     * @param bytes
     * @param start
     * @param length
     * @return
     */
    public static String getByteToStr(byte[] bytes, int start, int length) {
        try {
            return StringUtils.trim(new String(bytes, start, length, "UTF-8"));

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StringUtil.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    /**
     * 字节数组复制
     *
     * @param bytes
     * @param start
     * @param length
     * @return
     */
    public static byte[] getPartByte(byte[] bytes, int start, int length) {
        byte[] bytes_int = new byte[length];
        System.arraycopy(bytes, start, bytes_int, 0, length);
        return bytes_int;

    }

    /**
     * 验证str是否为null或者""
     *
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        if (str != null && !"".equals(str) && !str.equals("null")) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 二进制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    /**
     * 字符串转二进制
     *
     * @param str
     * @return
     */
    public static byte[] hex2byte(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1) {
            return null;
        }

        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }
}
