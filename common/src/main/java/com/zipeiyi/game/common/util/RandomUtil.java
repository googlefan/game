package com.zipeiyi.game.common.util;

import java.util.Random;

/**
 * Created by zhangxiaoqiang on 16/12/6.
 */
public class RandomUtil {

    public static int generateRandomIndex(int length){
        Random random = new Random();
        return random.nextInt((int) Math.ceil(length-0.8));
    }

    public static void main(String[] args){
        int length = 1;
        for(int i=0;i<=10;i++){
            System.out.println("random iterator " + i + "=" + generateRandomIndex(length));
        }

    }
}
