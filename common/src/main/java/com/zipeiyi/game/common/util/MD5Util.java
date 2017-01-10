package com.zipeiyi.game.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * 计算MD5值
 * 
 * @author isaac chen
 * 
 */
public class MD5Util {

    /**
     * Used building output as Hex
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };

    public static String getMD5ofStr(String s) {
        return md5(s);
    }

    /**
     * 根据content,key,按预定算法计算hash值.
     * 
     * @param text
     * @param key
     * @return 加密结果
     */
    public static String hash(String text, String key) {
        // 0.检验输入合法性
        if (text == null) {
            throw new IllegalArgumentException("text can't be null");
        }
        if (key == null) {
            throw new IllegalArgumentException("key can't be null");
        }

        // 1.令S=MD5(key);将text末尾填0至16字节的整数倍(n),将补0后的text按16字节分组
        // 为c(1),c(2),...c(n);令b(1),b(2),...b(n)为中间变量;令最终结果为hash.
        String S = md5(key);
        byte[] textData = text.getBytes();
        int len = textData.length;
        int n = (len + 15) / 16;
        byte[] tempData = new byte[n * 16];
        for (int i = len; i < n * 16; i++) {
            tempData[i] = 0;
        }
        System.arraycopy(textData, 0, tempData, 0, len);
        textData = tempData;
        String[] c = new String[n];
        for (int i = 0; i < n; i++) {
            c[i] = new String(textData, 16 * i, 16);
        }
        // end c
        String[] b = new String[n];
        String hash;

        // 2.计算b(i)
        // b(1)=MD5(S+c(1))
        // b(2)=MD5(b(1)+c(2))
        // ...
        // b(n)=MD5(b(n-1)+c(n))
        String temp = S;
        String target = "";
        for (int i = 0; i < n; i++) {
            b[i] = md5(temp + c[i]);
            temp = b[i];
            target += b[i];
        }

        // 3.hash=MD5(b(1)+b(2)+...+b(n))
        hash = md5(target);
        return hash;
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexidecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     * 
     * @param data
     *            a byte[] to convert to Hex characters
     * @return A char[] containing hexidecimal characters
     */
    private static char[] encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }

        return out;
    }

    private static MessageDigest getMD5Digest() {
        try {
            MessageDigest md5MessageDigest = MessageDigest.getInstance("MD5");
            md5MessageDigest.reset();
            return md5MessageDigest;
        }
        catch (NoSuchAlgorithmException nsaex) {
            throw new RuntimeException("Could not access MD5 algorithm, fatal error");
        }
    }

    /**
     * 计算content的md5摘要.
     * 
     * @param content
     * @return md5结果
     */
    private static String md5(String content) {
        byte[] data = getMD5Digest().digest(content.getBytes());
        char[] chars = encodeHex(data);
        return new String(chars);
    }
}
