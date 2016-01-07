package com.yunkouan.lpid.modbus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.lpid.commons.util.ResourceBundleUtil;
import com.yunkouan.lpid.modbus.service.SyncPLCService;

/**
 * @author yangli
 * 2015年12月6日 下午4:15:11
 */
@Controller
@RequestMapping(value="/modbus")
public class ModbusController {
	
	/**
	 * 显示行邮PLC参数一览及控制画面
	 * @return
	 */
	@RequestMapping(value="/monitor") 
	public String plcView(){
		return "/modbus/plctrace";
	}
	
	/**
	 * 皮带是否受范德兰德控制
	 * @param controlledFlag
	 */
	@RequestMapping(value="/beltControlled")
	@ResponseBody
	public void beltControlled(int controlledFlag){
		int plcId = ResourceBundleUtil.getModbusInt("modbus.controlled");
		SyncPLCService.getInstance().syncWritePlc(plcId, controlledFlag);
	}
	
	/**
	 * 向PLC发送数据
	 * @param plcId
	 * @param plcValue
	 */
	@RequestMapping(value="/plcHandle")
	@ResponseBody
	public boolean plcHandle(Integer plcId,Integer plcValue){
		try {
			SyncPLCService.getInstance().syncWritePlc(plcId, plcValue);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
