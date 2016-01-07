package com.yunkouan.lpid.biz.screenscompare.util.eh100;

import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkou.common.util.ResourceBundleUtil;
import com.yunkou.common.util.ThreadUtil;

/**
 * RFID读卡器连接线程
 * @author Administrator
 */
public class RfidLinkRunnable implements Runnable{
	private Logger logger = LoggerFactory.getLogger(getClass());
	public static  RFIDSocketClient readrfidClient;
	public static String tid;
	public static String epc;
	public static String getEpcAndTid(){
		return tid+","+epc;
	}
	
	public void run(){
		boolean linked = link();
		while(!linked)
			linked = link();
		/*logger.debug("启动外层读卡器读线程");
		try {	
			while(true){
				List list_tid = readrfidClient.readTid();
                if(list_tid==null){
				   continue;
				}
				List list_epc = readrfidClient.readEpc();
				if(list_epc==null){
					continue;
				}
				if(list_tid.size()>0 && list_epc.size()>0){
					tid = (String)list_tid.get(0);//tid
					epc = (String)list_epc.get(0);//epc
				}
		    }*/
		/*} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			link();
		}*/    
	}
	
	/**
	 * 返回socket连接的状态  
	 * 				true:已经连接
	 * 				false:没有连接
	 */
	public boolean link() {
		ThreadUtil.sleep(200);// 读卡器连接前休眠200毫秒
		
		String ip = ResourceBundleUtil.getSystemString("ReadRFID_IP").trim() ; 
		int port = ResourceBundleUtil.getSystemInt("ReadRFID_PORT"); 
		boolean isNeedConnected = true;//判断是否需要连接
		isNeedConnected = readrfidClient == null;
		if (readrfidClient != null) {
			try {
				readrfidClient.sendUrgentData(0x00);
				isNeedConnected = false;
			} catch (Exception e) {
				logger.error("RFID读卡器IP:{},PORT:{}连接失败!",ip,port);

				if (readrfidClient != null)
					try {
						readrfidClient.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				isNeedConnected = true;
			}
		}

		if (isNeedConnected) {
			try {
				readrfidClient = new RFIDSocketClient(ip, port);
				readrfidClient.setKeepAlive(true);
				logger.error("RFID读卡器IP:{},PORT:{}连接成功!",ip,port);
				return true;
			} catch (IOException e) {
				logger.error("RFID读卡器IP:{},PORT:{}连接失败!",ip,port);
				if (readrfidClient != null)
					try {
						readrfidClient.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				return false;
			} 
		} else {
			return true;
		}
	}	

	public static synchronized RFIDSocketClient getReadrfidClient() {
		return readrfidClient;
	}
}
