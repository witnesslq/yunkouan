/**  
* @Title: RfidNoReaderRunnable.java
* @Description:
* @Copyright: Copyright(c) 2015
* @author yangli 
* @date 2015年8月20日-下午3:32:39
* @version 1.0.0
*/ 
package com.yunkouan.lpid.biz.screenscompare.util.eh100;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.service.ScreenscompareService;
import com.yunkouan.lpid.biz.trace.bo.TraceTimeBo;
import com.yunkouan.lpid.persistence.entity.TraceTime;

import antlr.StringUtils;

/**
 * RFID号码读取工具类
 * @author yangli
 */
public class RfidNoReaderRunnable implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(RfidNoReaderRunnable.class);
	private static ScreenscompareService screenscompareService;
	private RunningPackageModel runningPackageModel;
	private long readTimeOut;// 读取标签超时时间
	private TraceTimeBo traceTimeBo;
	private Map<String,TraceTime> traceTimes;
	private long startRead ;
	public RfidNoReaderRunnable(RunningPackageModel runningPackageModel,long readTimeOut){
		this.runningPackageModel = runningPackageModel;
		this.readTimeOut = readTimeOut;
		
		screenscompareService = SpringApplicationContext.getBean(ScreenscompareService.class);
		this.traceTimeBo = SpringApplicationContext.getBean(TraceTimeBo.class);
		this.traceTimes = this.traceTimeBo.getTraceTimes();
	}
	
	@Override
	public void run() {
		try {
			logger.debug("6.==========读取标签号码>>>包裹编号：{}", runningPackageModel.getPackageNo());
			TraceTime traceTime = this.traceTimes.get(runningPackageModel.getPackageNo());
			startRead = System.currentTimeMillis();
			if(traceTime != null) traceTime.setReadEpcStartTime(startRead);
			readEpc();
			long endRead = System.currentTimeMillis();
			if(org.apache.commons.lang.StringUtils.isEmpty(runningPackageModel.getRfidNo())){
				logger.error("没有读取到RFID标签的EPC号码!规定读取超时时间为:{}",readTimeOut);
				logger.debug("endRead:{}-startRead:{}={}",new Object[]{endRead,startRead,(endRead-startRead)});
			}
//			readTid();
		} catch (IOException e) {
			logger.error("读取RFID出错!",e);
		}
	}
	/**
	 * @detail 读取EPC 
	 * @throws IOException
	 */
	public void readEpc() throws IOException {
		RFIDSocketClient client = RfidLinkRunnable.getReadrfidClient();
		if(client == null) return ;
		List<String> epcs = client.readEpc();
		if(client !=null && epcs != null && epcs.size() >0){
			runningPackageModel.setRfidNo(epcs.get(0));
			logger.debug("读取到epc号码为：{}" , epcs.get(0));// 45678920140101000510
			logger.debug("6.1 ==========读取到标签号码{}>>>包裹编号：{}",epcs.get(0),runningPackageModel.getPackageNo());
			
			TraceTime traceTime = this.traceTimes.get(runningPackageModel.getPackageNo());
			if(traceTime != null) traceTime.setReadEpcEndTime(System.currentTimeMillis());
			
			return ;
		} 
		// 4.2.4、保存数据
//		logger.debug("==========保存数据>>>包裹编号：{}",runningPackageModel.getPackageNo());
//		runningPackageModel.setCreateTime(new Date());
//		screenscompareService.saveRunningPackage(runningPackageModel);
	}
	/**
	 * @detail 读取TID 
	 * @throws IOException
	 */
	public void readTid() throws IOException {
		while(System.currentTimeMillis() < readTimeOut) {
			RFIDSocketClient client = RfidLinkRunnable.getReadrfidClient();
			List<String> tids = client.readTid();
			if(client !=null && tids != null && tids.size() >0){
				runningPackageModel.setRfidNo(tids.get(0));
				logger.debug("读取到tid号码为：{}" , tids.get(0));// 0CE2801105200056D664550252
			} else {
				logger.debug("没有读取到tid号码");
			}
			// 此处不需要休眠，因为在readTid()方法中已经休眠过了。
//			ThreadUtil.sleep(2000);
		}
	}
}
