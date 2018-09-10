package com.antifake.utils;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class FileUploadUtil {

	
	public List<Map<String, Object>> read(String fileName) throws org.json.simple.parser.ParseException, IOException {
		
		JSONParser parser = new JSONParser();

		
		//Map<Integer, Map<String, Object>> maps= new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
        try {

        	FileReader file= new FileReader("./src/main/resources/codes/unseen/"+fileName);
            Object obj = parser.parse(file);

            JSONArray json = (JSONArray) obj;
           
    
            
         
            Iterator<?> itr = json.iterator();
            int i=0;
           // itr.next();
            while(itr.hasNext())
            {
	            Map<String, Object> map= new HashMap<>();
	            itr.next();
	            JSONObject jo = (JSONObject) json.get(i);
	            
	            
	            map.put("uuid", jo.get("uuid"));
	            map.put("sign", jo.get("sign"));
	            
	            list.add(map);
	           // maps.put(i, map);
	            i++;
	            
	            if(itr.hasNext() == false)
		            {
	            	
		            file.close();
		            	  Files.move(Paths.get("./src/main/resources/codes/unseen/"+fileName), Paths.get("./src/main/resources/codes/seen/"+fileName));
		            }
            }
         
            
            
              
        } catch (IOException e) {
        	 Files.move(Paths.get("./src/main/resources/codes/unseen/"+fileName), Paths.get("./src/main/resources/codes/error/"+fileName));
        
            e.printStackTrace();
        } 
        
        
		return list;
       
		
	}
	
	public List<String> listFilesForFolder(final File folder) {
		List<String> list = new ArrayList<>();
		if(folder.exists())
		{
		
	    for (final File fileEntry : folder.listFiles()) {
	        
	    	list.add(fileEntry.getName());
	            
	        }
	   
	    }else {
	    	System.out.println("folder does not exists");
	    }
		
		 return list;
	}
	
	public static void main(String[] args) throws org.json.simple.parser.ParseException, IOException {
		
		FileUploadUtil fp= new FileUploadUtil();
		
		fp.listFilesForFolder(new File("./src/main/resources/codes/unseen/"));
		
		
	/*	Boolean bool=new File("./src/main/resources/codes/unseen/code.json").isFile();
		if(bool == true)
		{
		List<Map<String, Object>> list=fp.read("./src/main/resources/codes/unseen/code.json");
		
		for(int i=0;i<list.size();i++)
		{
			Map<String, Object> map=list.get(i);
			System.out.println(i+"uuid" + map.get("uuid"));
			System.out.println(i+"sign" + map.get("sign"));
			System.out.println(i+"msg" + map.get("msg"));
		}
		    
		}*/
	}

}
