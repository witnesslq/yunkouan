package com.yunkouan.lpid.modbus.model;

/**
 * @brief 操作plc内存的实体
 * @author Administrator
 *
 */
public class PLCOpreatorModel {
	private int plcId;
	private Integer value;
	
	public PLCOpreatorModel(int plcId, Integer value) {
		this.plcId = plcId;
		this.value = value;
	}
	
	public int getPlcId() {
		return plcId;
	}
	public void setPlcId(int plcId) {
		this.plcId = plcId;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
}
