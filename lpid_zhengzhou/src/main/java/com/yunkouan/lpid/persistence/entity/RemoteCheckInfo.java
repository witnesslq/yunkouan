package com.yunkouan.lpid.persistence.entity;



/**
 * 远程查验需要反馈的信息实体
 * @author Administrator
 *
 */
public class RemoteCheckInfo {

	//邮包id
	private String baggageId;
	
	//邮包图像
	private String baggageImg;
	
	public String getBaggageId() {
		return baggageId;
	}

	public void setBaggageId(String baggageId) {
		this.baggageId = baggageId;
	}

	public String getBaggageImg() {
		return baggageImg;
	}

	public void setBaggageImg(String baggageImg) {
		this.baggageImg = baggageImg;
	}
}
