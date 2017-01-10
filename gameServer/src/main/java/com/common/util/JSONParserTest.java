package com.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.stream.JsonReader;


public class JSONParserTest {
	
	public void readFile(String filepath) throws IOException{
		
		  File file = new File(filepath);
		  
		  if(file.isDirectory()){
			  parseStreamToObj(file);
		  }else{
			  String[] fileList = file.list();
			  for (int i = 0; i < fileList.length; i++) {			  
				     String path = file.getAbsolutePath()+"\\"+fileList[i];
				     readFile(path);				     
			  }	    
		  }
		  
	}
	
	public void parseStreamToObj(File file) throws IOException{
		
		InputStreamReader inreader = new InputStreamReader(new FileInputStream(file), "UTF-8");
		JsonReader jreader =  new JsonReader(inreader);
		
		while (jreader.hasNext()) {
//			jreader.
			
		}
	}

	public static void main(String[] args) throws FileNotFoundException{
		
			JSONParserTest t = new JSONParserTest();
			
			try {
				t.readFile("src/main/java/common/datum");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
	}
}
