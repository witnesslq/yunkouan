/**  
* @Title: RFID.java
* @Description:
* @Copyright: Copyright(c) 2015
* @author yangli 
* @date 2015年9月11日-下午5:30:02
* @version 1.0.0
*/ 
package com.yunkouan.lpid.rfid.model;

import org.springframework.stereotype.Component;

@Component
public class RFID {
	// EPC号码
	private String epc;

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}
}
