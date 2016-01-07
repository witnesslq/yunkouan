package com.yunkouan.lpid.modbus.service;


import static com.yunkou.common.util.ResourceBundleUtil.getModbusInt;
import static com.yunkou.common.util.ResourceBundleUtil.getModbusString;
import static com.yunkouan.lpid.modbus.model.PhototubeUseageEnum.PAGGAGE_LENGTH;
import static com.yunkouan.lpid.modbus.model.PhototubeUseageEnum.READ_PAGGAGE_NO;

import java.util.HashMap;
import java.util.Map;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.cloud.bo.CloudImageResult;
import com.yunkouan.lpid.biz.label.service.LabelService;
import com.yunkouan.lpid.biz.label.service.impl.LabelServiceImpl;
import com.yunkouan.lpid.biz.phototube.service.PhototubeService;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.modbus.model.MemoryModel;
import com.yunkouan.lpid.modbus.model.PhototubeUseageEnum;
import com.yunkouan.lpid.persistence.entity.Label;

/**
* @brief 通过modbus连接plc并且同步plc的内存
* @author Administrator
*/
public class SyncPLCService extends ModbusService{
	// 上一次标签标识
	private static int lastLabel = 0;
	
	/**
	 * @brief 控制绑定
	 */
	private static Map<Integer, PhototubeUseageEnum> controlPLCList;	
	private static final SyncPLCService syncPLCService = new SyncPLCService();
	public static Logger logger = LoggerFactory.getLogger(SyncPLCService.class);
	private static PhototubeService phototubeService;
	private MemoryModel memoryModel;
	public static SyncPLCService getInstance(){
		return syncPLCService;
	}
	
	private SyncPLCService() {
		super(getModbusString("modbus.server.host"), getModbusInt("modbus.server.port"));
		phototubeService = SpringApplicationContext.getBean(PhototubeService.class);
		memoryModel = SpringApplicationContext.getBean(MemoryModel.class);
		controlPLCList = new HashMap<Integer, PhototubeUseageEnum>();
		
		//初始化plc
		initPLCSerial();
		
		//初始化后建立连接
		connection();
	}
	
	/**
	 * 光电管和光电管用途绑定到列表，后面便于查找和遍历
	 */
	private static void initPLCSerial(){
//		controlPLCList.put(getModbusInt("modbus.belt_stop"), BELT_STOP);
//		controlPLCList.put(getModbusInt("modbus.xray_off"), XRAY_OFF);
//		controlPLCList.put(getModbusInt("modbus.xray_startup"), XRAY_STARTUP);
//		controlPLCList.put(getModbusInt("modbus.controlled"), CONTROLLED);
//		controlPLCList.put(getModbusInt("modbus.belt_startup"), BELT_STARTUP);
//		controlPLCList.put(getModbusInt("modbus.write.package_no"), WRITE_PAGGAGE_NO);
//		controlPLCList.put(getModbusInt("modbus.bag-push"), BAG_PUSH);
		controlPLCList.put(getModbusInt("modbus.read.package_no"), READ_PAGGAGE_NO);
		controlPLCList.put(getModbusInt("modbus.package_length"), PAGGAGE_LENGTH);
//		controlPLCList.put(getModbusInt("modbus.read.package_no2"), READ_PAGGAGE_NO2);
//		controlPLCList.put(getModbusInt("modbus.putters.push.status"), PUTTERS_PUSH);
//		controlPLCList.put(getModbusInt("modbus.putters.return.status"), PUTTERS_RETURN);
//		controlPLCList.put(getModbusInt("modbus.decals.status"), DECALS);
//		controlPLCList.put(getModbusInt("modbus.far_on-off.status"), FAR_ONOFF);
//		controlPLCList.put(getModbusInt("modbus.near_on-off.status"), NEAR_ONOFF);
//		controlPLCList.put(getModbusInt("modbus.energy.status"), ENERGY);
//		controlPLCList.put(getModbusInt("modbus.first_belt.run.status"), FIRSTBELT_RUN);
//		controlPLCList.put(getModbusInt("modbus.second_belt.run.status"), SECONDBELT_RUN);
//		controlPLCList.put(getModbusInt("modbus.x-ray_machine.run.status"), XRAY_MACHINE_RUN);
//		controlPLCList.put(getModbusInt("modbus.stop.status"), STOP);
//		controlPLCList.put(getModbusInt("modbus.all.status"), ALL);
//		controlPLCList.put(getModbusInt("modbus.putters.origin.status"), PUTTERS_ORIGIN);
//		controlPLCList.put(getModbusInt("modbus.origin.on-off.status"), ORIGIN_ONOFF);
//		controlPLCList.put(getModbusInt("modbus.bag-pushing.status"), BAG_PUSHING);
	}
	
	/**
	 * @brief 同步plc内存
	 * @throws ErrorResponseException 
	 * @throws ModbusTransportException 
	 */
	public synchronized void syncReadPlc() throws ModbusTransportException, ErrorResponseException{
		//同步plc监控部分状态
		BatchResults<Number> monitoerResults = syncPlc(controlPLCList);
		
		//更新内存并调用handler
		boolean packageNoFlag = compareAndChangePLCMemory(monitoerResults);
//		if(packageNoFlag) {
//			int serialNo =  getModbusInt("modbus.package_length");
//			
//			//同步plc监控部分状态
//			monitoerResults = syncPlc(controlPLCList);
//			
//			//plc最新的状态
//			Integer currentValue = (Integer)monitoerResults.getValue(serialNo);
//			
//			//更新内存
//			memoryModel.setValueBySerialNo(controlPLCList.get(serialNo), currentValue);
//			// 如果包裹编号的值为0,或者包裹的长度为0,则返回上次调用.
//			if (currentValue == 0)
//				return;
//			logger.debug("syncReadPlc()");
//			//调用handler(包裹编号发生变化)
//			phototubeService.handle(controlPLCList.get(serialNo),currentValue);
//		}
	}

	/**
	 * @brief plc数据同步
	 * @return
	 * @throws ErrorResponseException 
	 * @throws ModbusTransportException 
	 */
	private BatchResults<Number> syncPlc(Map<Integer, PhototubeUseageEnum> plcs) throws ModbusTransportException, ErrorResponseException{
		BatchRead<Number> batchRead = new BatchRead<Number>();
        int slaveId = 1;
		for (Integer plcSerial : plcs.keySet()) {
			//逻辑id和光电管绑定，
        	batchRead.addLocator(plcSerial, BaseLocator.holdingRegister(slaveId, plcSerial, DataType.TWO_BYTE_INT_UNSIGNED));
        }
      
		//数据同步
		return master.send(batchRead);
	}
	
	
	/**
	 * 修改plc内存中的值，该值保持和plc同步
	 */
	private synchronized boolean compareAndChangePLCMemory(BatchResults<Number> results){
		boolean result = false;
		for(Integer serialNo : controlPLCList.keySet()){
			//内存中的状态
			Integer latestPlcValue = memoryModel.getValueBySerialNo(controlPLCList.get(serialNo));
			//plc最新的状态
			Integer currentValue = (Integer)results.getValue(serialNo);
			//当前值和最新值相同则不处理
			if(latestPlcValue != null && latestPlcValue.intValue() == currentValue.intValue()){
				continue;
			}
			
			//更新内存
			memoryModel.setValueBySerialNo(controlPLCList.get(serialNo), currentValue);
			// 如果包裹编号的值为0,或者包裹的长度为0,则继续下一次循环.
			if(currentValue ==0) continue;
			logger.debug("compareAndChangePLCMemory()");
			
			//调用handler(包裹编号发生变化)
			if(controlPLCList.get(serialNo) == PhototubeUseageEnum.READ_PAGGAGE_NO || controlPLCList.get(serialNo) == PhototubeUseageEnum.PAGGAGE_LENGTH ) {
				phototubeService.handle(controlPLCList.get(serialNo),currentValue);
			}
			
			// 推送标签数量
			if(controlPLCList.get(serialNo) == PhototubeUseageEnum.LABEL_NUMBER) {
				if(currentValue != lastLabel && currentValue == 1) {
					pushLabelMessage();
				}
				
				lastLabel = currentValue;
			}
			
			result = true;
		}
		return result;
	}
	
	/**
	 * 推送标签数量
	 */
	private void pushLabelMessage() {
		LabelService labelService = SpringApplicationContext.getBean(LabelServiceImpl.class);
		// 检索标签信息
		Label label = labelService.findLabel();
		// 更新标签信息(剩余标签数量 = 剩余标签数量  -1)
		label.setBalance(label.getBalance() - 1);
		labelService.updateBalance(label);
		// 推送标签
		CloudImageResult cir = new CloudImageResult();
		cir.setValue(label);
		cir.setType("label");
		Gson gson = new Gson();
		String json = gson.toJson(cir);
		
		BroadcasterFactory broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
    	//通过broadcasterFactory对象获取查验频道对象
    	Broadcaster broadcaster = broadcasterFactory.lookup(DefaultBroadcaster.class, Constant.CHANNEL_A, true);
    	//向查验频道的订阅者发送算法计算后的坐标
    	broadcaster.broadcast(json);
	}

	/**
	 * @brief 向指定plcId的plc中写value
	 * @param plcId
	 * @param value
	 * @return
	 * @throws ErrorResponseException 
	 * @throws ModbusTransportException 
	 */
	public BaseLocator<Number> syncWritePlc(int plcId, Integer value) {
		BaseLocator<Number> baseLocator = BaseLocator.holdingRegister(plcId, plcId, DataType.TWO_BYTE_INT_UNSIGNED);
		try {
            master.setValue(baseLocator, value);
            logger.debug("向Plc {} 写入 {}", plcId, value);
		} catch (ModbusTransportException | ErrorResponseException e) {
            logger.error("写PLC发生错误，plcId:{" + plcId + "},value:{" + value + "}", e);
        }
		
		return baseLocator;
	}		
//	
//	public static void main(String[] args) throws ModbusTransportException, ErrorResponseException {
//		SyncPLCService.init();
//		BatchResults<Number> results = SyncPLCService.syncReadPlc(1, 20);
//		logger.debug(results.getValue(5));
//		SyncPLCService.syncWritePlc(5, 1);
//		BatchResults<Number> result1 = SyncPLCService.syncReadPlc(1, 20);
//		logger.debug(result1.getValue(5));
//	}
	
}
