package com.yunkouan.lpid.modbus.model;

import org.springframework.stereotype.Component;

/**
 * <P>Title: lpid-zhengzhou</P>
 * <P>Description: 光电管信号在内存中的实时值
 *    读取方式：定时读取光电管的值，保存在MemoryModel中
 * </P>
 * <P>Copyright: Copyright(c) 2015</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2015年7月23日-下午5:39:12</P>
 * @author lify
 * @version 1.0.0
 */
@Component
public class MemoryModel {
	private Integer writePaggageNo = 0;
	private Integer readPaggageNo = 0;
	private Integer paggageLength = 0;
	private Integer beltStop = 0;
	private Integer xrayOff = 0;
	private Integer xrayStartup = 0;
	private Integer controlled = 0;
	private Integer beltStartup = 0;
	private Integer bagPush = 0;
	private Integer packageNo2 = 0;
	private Integer puttersPush = 0;
	private Integer puttersReturn = 0;
	private Integer decals = 0;
	private Integer farOnOff = 0;
	private Integer nearOnOff = 0;
	private Integer energy = 0;
	private Integer firstBeltRun = 0;
	private Integer secondBeltRun = 0;
	private Integer xrayMachineRun = 0;
	private Integer stop = 0;
	private Integer all = 0;
	private Integer puttersOrigin = 0;
	private Integer originOnOff = 0;
	private Integer bagPushing = 0;
	private Integer labelNumber = 0;
	
	public Integer getValueBySerialNo(PhototubeUseageEnum phototubeUseageModel) {
		switch (phototubeUseageModel) {
		case WRITE_PAGGAGE_NO:
			return writePaggageNo;
		case READ_PAGGAGE_NO:
			return readPaggageNo;
		case PAGGAGE_LENGTH:
			return paggageLength;
		case BELT_STOP:
			return beltStop;
		case XRAY_OFF:
			return xrayOff;
		case XRAY_STARTUP:
			return xrayStartup;
		case CONTROLLED:
			return controlled;
		case BELT_STARTUP:
			return beltStartup;
		case BAG_PUSH:
			return bagPush;
		case READ_PAGGAGE_NO2:
			return packageNo2;
		case PUTTERS_PUSH:
			return puttersPush;
		case PUTTERS_RETURN:
			return puttersReturn;
		case DECALS:
			return decals;
		case FAR_ONOFF:
			return farOnOff;
		case NEAR_ONOFF:
			return nearOnOff;
		case ENERGY:
			return energy;
		case FIRSTBELT_RUN:
			return firstBeltRun;
		case SECONDBELT_RUN:
			return secondBeltRun;
		case XRAY_MACHINE_RUN:
			return xrayMachineRun;
		case STOP:
			return stop;
		case ALL:
			return all;
		case PUTTERS_ORIGIN:
			return puttersOrigin;
		case ORIGIN_ONOFF:
			return originOnOff;
		case BAG_PUSHING:
			return bagPushing;
		case LABEL_NUMBER:
			return labelNumber;
		default:
			return null;
		}
	}
   
	public void setValueBySerialNo(PhototubeUseageEnum phototubeUseageModel, Integer value){
		switch (phototubeUseageModel) {
		case WRITE_PAGGAGE_NO:
			writePaggageNo = value;
			break;
		case READ_PAGGAGE_NO:
			readPaggageNo = value;
			break;
		case PAGGAGE_LENGTH:
			paggageLength = value;
			break;
		case BELT_STOP:
			beltStop = value;
			break;
		case XRAY_OFF:
			xrayOff = value;
			break;
		case XRAY_STARTUP:
			xrayStartup = value;
			break;
		case CONTROLLED:
			controlled = value;
			break;
		case BELT_STARTUP:
			beltStartup = value;
			break;
		case BAG_PUSH:
			bagPush = value;
			break;
		case READ_PAGGAGE_NO2:
			packageNo2 = value;
			break;
		case PUTTERS_PUSH:
			puttersPush = value;
			break;
		case PUTTERS_RETURN:
			this.puttersReturn = value;
			break;
		case DECALS:
			this.decals = value;
			break;
		case FAR_ONOFF:
			this.farOnOff = value;
			break;
		case NEAR_ONOFF:
			this.nearOnOff = value;
			break;
		case ENERGY:
			this.energy = value;
			break;
		case FIRSTBELT_RUN:
			this.firstBeltRun = value;
			break;
		case SECONDBELT_RUN:
			this.secondBeltRun = value;
			break;
		case XRAY_MACHINE_RUN:
			this.xrayMachineRun = value;
			break;
		case STOP:
			this.stop = value;
			break;
		case ALL:
			this.all = value;
			break;
		case PUTTERS_ORIGIN:
			this.puttersOrigin = value;
			break;
		case ORIGIN_ONOFF:
			this.originOnOff = value;
			break;
		case BAG_PUSHING:
			this.bagPushing = value;
			break;
		case LABEL_NUMBER:
			this.labelNumber = value;
			break;
		default:
			break;
		}
	}
	
	/**
	 * Integer为null时候转换成0
	 * 
	 * @param value
	 * @return 
	 * @since 1.0.0
	 * 2015年6月17日-下午4:54:22
	 */
	private int getIntegerValue(Integer value) {
	    return value == null? 0 : value;
	}
}
