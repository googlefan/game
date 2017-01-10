/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zipeiyi.game.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.Arrays;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

/**
 *
 * @author LXF 二进制互转方法
 */
public class TranscodUtiling {

    private static String hexStr = "0123456789ABCDEF";

    //整数到字节数组的转换 
    public static byte[] intToByte(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = new Integer(temp & 0xff).byteValue();      //将最高位保存在最低位  
            temp = temp >> 8;       //向右移8位  
        }
        return b;
    }
    //字节数组到整数的转换 

    public static int byteToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[i] >= 0) {
                s = s + b[i];
            } else {
                s = s + 256 + b[i];
            }
            s = s * 256;
        }
        if (b[3] >= 0) //最后一个之所以不乘，是因为可能会溢出  
        {
            s = s + b[3];
        } else {
            s = s + 256 + b[3];
        }
        return s;
    }
    //字符到字节转换 

    public static byte[] charToByte(char ch) {
        int temp = (int) ch;
        byte[] b = new byte[2];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = new Integer(temp & 0xff).byteValue();      //将最高位保存在最低位  
            temp = temp >> 8;       //向右移8位  
        }
        return b;
    }
    //字节到字符转换 

    public static char byteToChar(byte[] b) {
        int s = 0;
        if (b[0] > 0) {
            s += b[0];
        } else {
            s += 256 + b[0];
        }
        s *= 256;
        if (b[1] > 0) {
            s += b[1];
        } else {
            s += 256 + b[1];
        }
        char ch = (char) s;
        return ch;
    }
    //浮点到字节转换 

    public static byte[] doubleToByte(double d) {
        byte[] b = new byte[8];
        long l = Double.doubleToLongBits(d);
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(l).byteValue();
            l = l >> 8;
        }
        return b;
    }
    //字节到浮点转换

    public static double byteToDouble(byte[] b) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;

        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);

        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    /**
     * 将byte[]转换成string
     *
     * @param butBuffer
     */
    public static String byteToString(byte[] b) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            stringBuffer.append((char) b[i]);
        }
        return stringBuffer.toString();
    }

    /**
     * 将string转换成IoBuffer
     *
     * @param str
     */
    
    /**
     * 将byte []转换成IoBuffer
     *
     * @param str
     */
   
    /**
     * 将IoBuffer转换成byte []
     *
     * @param Object
     */
    

    /**
     * 将IoBuffer转换成string
     *
     * @param butBuffer
     */
   

    //long类型转成byte数组 
    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();//将最低位保存在最低位 
            temp = temp >> 8;// 向右移8位 
        }
        return b;
    }

    //byte数组转成long 
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位 
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位 
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变 
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * 注释：short到字节数组的转换！
     *
     * @param s
     * @return
     */
    public static byte[] shortToByte(short number) {
        int temp = number;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位 
            temp = temp >> 8;// 向右移8位 
        }
        return b;
    }

    /**
     * 注释：字节数组到short的转换！
     *
     * @param b
     * @return
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位 
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * 注释：将流文件转换成字节
     *
     * @param InputStream is
     * @return
     */
    public static byte[] getByteFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    /**
     * 注释：将字节转化成流文件
     *
     * @param byte[] bye
     * @return
     */
    public static InputStream getInputStreamFromByte(byte[] bye) {
        InputStream is = new ByteArrayInputStream(bye);
        return is;
    }

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
//进行序列化

    public static byte[] getByteArray(byte[] str) {
        byte[] bt = (byte[]) null;
        try {
            if (str != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(str);
                bt = bos.toByteArray();
            }
        } catch (Exception ex) {
            bt = (byte[]) null;
            ex.printStackTrace();
        }
        return bt;
    }

    //进行反序列化
    public static byte[] getArrayList(byte[] bt) {
        byte[] Array;
        ObjectInputStream objIps;
        try {
            objIps = new ObjectInputStream(
                    new ByteArrayInputStream(bt));
            Object o = objIps.readObject();

            Array = (byte[]) o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return Array;
    }

    public static int bytesToInt(byte[] data, int offset) {
        int num = 0;
        for (int i = offset; i < offset + 4; i++) {
         num <<= 8;
         num |= (data[i] & 0xff);
        }
        return num;
     }
 /**
  * 将整形转化成字节
  * @param num
  * @return
  */
 public static byte[] intToBytes(int num) {  
     byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
         b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
 }

    public static <T> T decode(byte[] datas, Class<?> t) throws IOException {
        Codec simpleTypeCode = ProtobufProxy.create(t);
        return (T) simpleTypeCode.decode(datas);
    }

    public static byte[] encode(Object obj, Class<?> t) throws IOException {
        Codec simpleTypeCode = ProtobufProxy.create(t);
        return simpleTypeCode.encode(obj);
    }

//    public static void main(String[] args){
//     int num = 8192*2*2*2;
//     System.out.println(Arrays.toString(intToByte(num)));
//     System.out.println(bytesToInt(intToByte(num),0));
//    }

}
