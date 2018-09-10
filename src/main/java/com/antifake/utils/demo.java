package com.antifake.utils;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class demo {
	
	public static void main(String [] args) {
		String a="";
		String host="www.mlnode.com";
	    String user="root";
	    String password="Itelly0u";
	    String command1="openssl ecparam -genkey -name prime256v1 -noout";
	    try{
	    	
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	Session session=jsch.getSession(user, host, 22);
	    	session.setPassword(password);
	    	session.setConfig(config);
	    	session.connect();
	    	System.out.println("Connected");
	    	
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command1);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        
	        channel.connect();
	        
	        BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
			String line;
			
			while(true){
			
				try{ 
					 br = new BufferedReader(new InputStreamReader(in));
					 while ((line = br.readLine()) != null) 
					 {
						 System.out.println(line); 
					 sb.append(line);
					 }
	
				   } catch (IOException e)
				   {
					e.printStackTrace();
				   } finally {
						if (br != null) {
							try {
								in.close();
								br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
			

	     /*   byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            a=new String(tmp, 0, i);
	            System.out.print(a);
	          }*/
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(500);}catch(Exception ee){}
	        }
	        channel.disconnect();
	        session.disconnect();
	        System.out.println("DONE");
	        
	        System.out.println(sb);
	        
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		
		
	}

}
