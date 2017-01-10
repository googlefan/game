package com.common.util;

import java.util.Random;

public class Utils {

	
	public static int randInt(int start,int end){
		Random random = new Random();
		return random.nextInt(end)+start;
	}
	
	public static String getRandomString(int len){
		String[] arr = { "A", "B", "C", "D", "E", "F", "G", "H", "I",  
	            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
	            "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i",  
	            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",  
	            "w", "x", "y", "z" };
		StringBuffer b = new StringBuffer();
		for(int i=0;i<len;i++){
			int slot = randInt(0,arr.length-1);
			b.append(arr[slot]);
		}
		
		return b.toString();		
	}
	
	
}
