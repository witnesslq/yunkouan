package com.yunkouan.lpid.biz.screenscompare.util.eh100;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class RFIDSocketClientFather extends Socket {
	public RFIDSocketClientFather(String ip,int port) throws UnknownHostException, IOException{
   	 	super(ip,port);
    }
	/**
	 * 获得读或写的状态
	 * @param result
	 * @return
	 */
    public boolean getReadOrWriteStates(String result){
    	
    	boolean flag = false;
    	String[] str = result.split(" ");
    	if(str[str.length-1].trim().equals("Command") || str[str.length-1].trim().equals("FAIL")){
    		flag = true;
    	}
    	return flag;
    }
}
