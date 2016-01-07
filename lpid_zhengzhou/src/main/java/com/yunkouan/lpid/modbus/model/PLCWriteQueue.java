package com.yunkouan.lpid.modbus.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @breif 需要修改的plc的队列
 * @author Administrator
 *
 */
public class PLCWriteQueue {
	private static Logger logger = LogManager.getLogger(PLCWriteQueue.class);
	
	private PLCWriteQueue(){}; 
	
	private static final PLCWriteQueue plcWriateQueue = new PLCWriteQueue();
	
	  //静态工厂方法   
    public static PLCWriteQueue getInstance() {  
        return plcWriateQueue;  
    } 
	
	/** 消息队列 **/
//	private BlockingQueue<PLCOpreatorModel> plcBaseLocatorWriteQueue = new LinkedBlockingQueue<PLCOpreatorModel>();///<消息队列
	private List<PLCOpreatorModel> plcBaseLocatorWriteQueue = new ArrayList<PLCOpreatorModel>();///<消息队列
	
	public static BlockingQueue<PLCOpreatorModel> plcChageQueue = new ArrayBlockingQueue<PLCOpreatorModel>(100);
	
	
	/**
	 * @brief 增加消息到消息队列
	 * @details
	 * -# 增加消息到消息队列
	 * -# 唤醒获取消息队列的线程
	 * @param msg
	 */
	public void putMsg(PLCOpreatorModel plcOpreatorModel){
		synchronized (plcBaseLocatorWriteQueue) {
			plcBaseLocatorWriteQueue.add(plcOpreatorModel);
			logger.debug(plcOpreatorModel);
			plcBaseLocatorWriteQueue.notifyAll();
		}
	}
	
	/**
	 * @brief 获取平台消息
	 * @details
	 * -# 获取平台消息
	 * -# 如果平台消息为空，则线程休眠等待
	 * @return
	 */
	public PLCOpreatorModel getMsg(){
		PLCOpreatorModel plcOpreatorModel = null;
		synchronized (plcBaseLocatorWriteQueue) {
			if(plcBaseLocatorWriteQueue.isEmpty()){
				try {
					plcBaseLocatorWriteQueue.wait();
				} catch (InterruptedException e) {
					logger.error( e.getMessage(), e);
				}
			}
			if(plcBaseLocatorWriteQueue.size() > 0){
				//取出并消费,没有返回null
//				plcOpreatorModel = plcBaseLocatorWriteQueue.poll();
				plcOpreatorModel = plcBaseLocatorWriteQueue.get(0);
				plcBaseLocatorWriteQueue.remove(0);
			}
			logger.debug(plcOpreatorModel.toString());
			return plcOpreatorModel;
		}
	}
}
