package com.yunkouan.lpid.modbus.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.yunkou.common.util.ThreadUtil;
import com.yunkouan.lpid.modbus.model.PLCOpreatorModel;
import com.yunkouan.lpid.modbus.model.PLCWriteQueue;
import com.yunkouan.lpid.modbus.service.SyncPLCService;

/**
 * @breif PLC相关线程工厂类(当前类包括3个线程类)
 * @author YangLi
 */
public class SyncPLCRunnable{
	protected static Logger logger = LoggerFactory.getLogger(SyncPLCReaderRunnable.class);
	//当前对象单一实例
	private static final SyncPLCRunnable syncPLCRunnable = new SyncPLCRunnable();
	//PLC的写队列
	private PLCWriteQueue queue = PLCWriteQueue.getInstance();
	//私有构造函数，说明只能在内部创建类的实例
	private SyncPLCRunnable(){
	}
	// 静态工厂方法
	// 用来创建当前类的单一实例对象
    public static SyncPLCRunnable getInstance() {
        return syncPLCRunnable;  
    }
	
	/**
	 * @brief 批量同步plc内存
	 * @author Administrator
	 */
	public class SyncPLCReaderRunnable implements Runnable{
		@Override
		public void run() {
			ThreadUtil.sleep(10000);
			// 无线循环读取PLC的值
			while(true){
				try {
					SyncPLCService.getInstance().syncReadPlc();
				} catch (ModbusTransportException e) {
					SyncPLCService.getInstance().connection();
					logger.error("断线重连中。。。。");
				} catch (ErrorResponseException e) {
					SyncPLCService.getInstance().connection();
					logger.error("断线重连中。。。。");
				}finally{
					ThreadUtil.sleep(100);
				}
			}
		}
	}

	/**
	 * @brief 消费者向plc发送修改指令
	 * @author Administrator
	 *
	 */
	public class SyncPLCWriterRunnable implements Runnable{
		@Override
		public void run() {
			while(true){
				PLCOpreatorModel plcOpreatorModel = queue.getMsg();
				if(plcOpreatorModel == null){
					continue;
				}
				try {
					SyncPLCService.getInstance().syncWritePlc(plcOpreatorModel.getPlcId(), plcOpreatorModel.getValue());
				} finally{
					ThreadUtil.sleep(100);
				}
			}
		}
	}
	
	/**
	 * @breif 需要修改plc的对象放到PLC消费队列中
	 * @author Administrator
	 *
	 */
//	public class ReadyToConsumeRunnable implements Runnable{
//
//		@Override
//		public void run() {
//			while(true){
//				if(!PLCWriteQueue.plcChageQueue.isEmpty()){
//					PLCOpreatorModel plcOpreatorModel = PLCWriteQueue.plcChageQueue.poll();
//					queue.putMsg(plcOpreatorModel);
//				}
//			}
//		}
//	}
	

//	public static void main(String[] args) {
//		SyncPLCRunnable syncPLCRunnable = new SyncPLCRunnable();
//		new Thread(syncPLCRunnable.new SyncPLCReaderRunnable()).start();
//		new Thread(syncPLCRunnable.new SyncPLCWriterRunnable()).start();
//		new Thread(syncPLCRunnable.new ReadyToConsumeRunnable()).start();
//	}
	
}
