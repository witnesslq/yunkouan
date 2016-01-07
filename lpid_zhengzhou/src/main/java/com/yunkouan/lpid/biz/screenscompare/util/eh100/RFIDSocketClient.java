package com.yunkouan.lpid.biz.screenscompare.util.eh100;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkou.common.util.ResourceBundleUtil;
import com.yunkou.common.util.ThreadUtil;

public class RFIDSocketClient extends RFIDSocketClientFather {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private final int tidLeng = 26;
	private final int ecpLeng = 28;
	private static final byte[] send = new byte[] { 'U', 'S', 'R', '_', 'L', 'I', 'S', 'T', 'C', 'A', 'R', 'D', 0x0d, 0x0a };
	public RFIDSocketClient(String ip, int port) throws UnknownHostException,
			IOException {
		super(ip, port);
	}

	/**写入rpc 
	 * @param rpc  rpc为24位
	 * @return
	 * @throws IOException 
	 */
	public Boolean writeEpc(String epc) throws IOException {
		logger.debug("writeEpc");
			//基础指令
			StringBuffer StringBuffer = new StringBuffer("USR_CHANGEID ");
			StringBuffer.append(epc);
			byte[] bytes = new byte[StringBuffer.length() + 2];
			System.arraycopy(StringBuffer.toString().getBytes(), 0, bytes, 0,
					StringBuffer.length());
			bytes[StringBuffer.length()] = 0x0d;
			bytes[StringBuffer.length() + 1] = 0x0a;
			this.getOutputStream().write(bytes);
			int count=0;
			DataInputStream input = new DataInputStream(this.getInputStream());
			while (true) {
				if (input.available() > 0) {
					byte[] readBytes = new byte[input.available()];
					this.getInputStream().read(readBytes);
					
					//返回信息的命令部分,不小于12位否则无效				
					if(readBytes.length<12){
						count=0;
						continue;
					}
					String result = new String(readBytes);
					String[] strs = result.split(" ");
					if("USR_CHANGEID".equals(strs[0])){
						if("FAIL".equals(strs[1].trim())){
							return false;//写入失败
						}else if("OK".equals(strs[strs.length-1].trim())){
//							logger.debug("写如成功"+result);
							return true;//写入成功
						}
						
					}else{
						count=0;
						continue;
					}
				}else{
					count++;
					ThreadUtil.sleep(20);
					//如果150毫秒中无法获取数据，数据超时，忽略该数据
					if(count>8){
						return false;
					}
				}
			}
	}

	
	/**读取EPC时间间隔大约为50-70毫秒
	 * @return
	 * @throws IOException
	 * ------------------------------------------------
	 * 修改人                                  修改时间                                                             修改内容
	 * 唐辉煌                 Nov 7, 2013 9:27:22 AM
	 */
	public List<String> readEpc() throws IOException {
		this.getOutputStream().write(send);
		ThreadUtil.sleep(500);
		
		DataInputStream input = new DataInputStream(this.getInputStream());
		List<String> ecpList2 = new ArrayList<String>();
		int count=0;
		long startRead = System.currentTimeMillis();
		long endRead = 0;
		long readTimeOut = ResourceBundleUtil.getSystemInt("RFID_READ_TIMEOUT");
		logger.debug("readEpc start:{}" , startRead);
//		while (true) {
		while((System.currentTimeMillis() - startRead) < readTimeOut){	
			if (input.available() > 0) {
				byte[] bytes = new byte[input.available()];
				input.read(bytes);
				//返回信息的命令部分,不小于12位否则无效				
				if(bytes.length<12){
					count=0;
					continue;
				}
				
				String result = new String(bytes);
				String[] strs = result.split(" ");
				if("USR_LISTCARD".equals(strs[0])){
					if("FAIL".equals(strs[1].trim())){
//						logger.debug("退出readEpc(),ecpList2.size={}" , ecpList2.size());
						endRead = System.currentTimeMillis();
						logger.debug(ecpList2.toString());
						logger.debug("readEpc End2:{}" , endRead);
						logger.debug("read time total2:{}" , (endRead - startRead));
						return ecpList2;
					}
					int number = 0;
					try{
						number = Integer.parseInt(strs[1]);	
						if(number == 0) {
							this.getOutputStream().write(send);
							continue;
						}
					}catch(NumberFormatException e) {
						logger.error("读取RFID标签的EPC号码出错!",e);
					}
					
					for (int i = 0; i < number; i++) {
						ecpList2.add(strs[2 + i].substring(4));
					}
					logger.debug("str.................");
					logger.debug(Arrays.asList(strs).toString());
					logger.debug("str.................");
					
//					logger.debug("退出readEpc(),ecpList2.size={}" , ecpList2.size());
					endRead = System.currentTimeMillis();
					logger.debug(ecpList2.toString());
					logger.debug("readEpc End3:{}" , endRead);
					logger.debug("read time total3:{}" , (endRead - startRead));
					return ecpList2;
				}else{
					count=0;
					continue;
				}
			}
//			else{
//				count++;
//				ThreadUtil.sleep(10);
//				
//				//如果100毫秒中无法获取数据，数据超时，忽略该数据
//				if(count>5){
//					return ecpList2;
//				}
//			}
		}
		endRead = System.currentTimeMillis();
		logger.debug("readEpc End:{}" , endRead);
		logger.debug("read time total:{}" , (endRead - startRead));
		return ecpList2;

		/* InputStream input=this.getInputStream();
		 //读取返回值  命令号：11位字节   + 一个空字符    读取个数 一个字节+一个空字符
		 byte[] header=new byte[15];
		 input.read(header);
		 String headerStr=new String(header);
		 int number=Integer.parseInt(headerStr.substring(13).trim());
		 //读取Tid
		 List ecpList=new ArrayList();
		 for(int i=0;i<number;i++){
			 byte[] ecpBytes=new byte[ecpLeng+1];
			 input.read(ecpBytes);
			 String tid=new String(ecpBytes).trim().substring(4);
			 ecpList.add(tid);
		 }
		 //读取剩余项目
		 byte[] lastBytes=new byte[4];
		 input.read(lastBytes);
		 byte[] lastTest=new byte[]{'O','K',0x0d,0x0a};
		 for(int i=0;i<lastBytes.length;i++){
			 if(lastTest[i]!=lastBytes[i]){
				 return null;
			 }
		 }    	 */
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<String> readTid() throws IOException {
		logger.debug("进入readTid()");
		byte[] send = new byte[] { 'U', 'S', 'R', '_', 'L', 'I', 'S', 'T', 'T',
				'I', 'D', 0x0d, 0x0a };
		this.getOutputStream().write(send);
		ThreadUtil.sleep(20);
		DataInputStream input = new DataInputStream(this.getInputStream());
		List<String> tidList2 = new ArrayList<String>();
		int count=0;
		while (true) {
			if (input.available() > 0) {
				byte[] bytes = new byte[input.available()];
				input.read(bytes);
				//心跳数据忽略
				if(bytes.length<11){
					count=0;
					continue;
				}
				String result = new String(bytes);
				String[] strs = result.split(" ");
				if("USR_LISTTID".equals(strs[0])){
					if("FAIL".equals(strs[1].trim())){
						logger.debug("退出readTid(),tidList2.size={}" , tidList2.size());
						return tidList2;
					}
					int number = Integer.parseInt(strs[1]);
					for (int i = 0; i < number; i++) {
						tidList2.add(strs[2 + i]);
					}
					logger.debug("退出readTid(),tidList2.size={}" , tidList2.size());
					return tidList2;
				}//错乱数据忽略
				else{
					count=0;
					continue;
				}
			}else{
				count++;
				ThreadUtil.sleep(10);
				//如果100毫秒中无法获取数据，数据超时，忽略该数据
				if(count>5){
					logger.debug("退出readTid(),tidList2.size={}" , tidList2.size());
					return tidList2;
				}
			}
		}
		/* InputStream input=this.getInputStream();
		 //读取返回值  命令号：11位字节   + 一个空字符    读取个数 一个字节+一个空字符
		 byte[] header=new byte[14];
		 input.read(header);
		 String headerStr=new String(header);
		 int number=Integer.parseInt(headerStr.substring(12).trim());
		 //读取Tid
		 List tidList=new ArrayList();
		 for(int i=0;i<number;i++){
			 byte[] tidBytes=new byte[tidLeng+1];
			 input.read(tidBytes);
			 String tid=new String(tidBytes).trim();
			 tidList.add(tid);
		 }
		 //读取剩余项目
		 if(number==0){
			 input.read(new byte[1]);
		 }
		 byte[] lastBytes=new byte[4];
		 input.read(lastBytes);
		 byte[] lastTest=new byte[]{'O','K',0x0d,0x0a};
		 for(int i=0;i<lastBytes.length;i++){
			 if(lastTest[i]!=lastBytes[i]){
				 return null;
			 }
		 }
		 */
		
	}
}
