/**  
* @Title: XrayImageMode.java
* @Description:
* @Copyright: Copyright(c) 2015
* @author  yangli 
* @date 2015年7月24日-下午6:16:20
* @version 1.0.0
*/ 
package com.yunkouan.lpid.biz.screenscompare.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * 存储X光图片Model类的组件
 */
@Component
public class XrayImageComponent {
	private final static Object synObject = new Object(); 
	private List<XrayImageModel> xrayImageModels = new ArrayList<XrayImageModel>();
	private long beginUploadTime;// 文件开始上传时间
	private long generateTime ;// 文件结束上传时间
	
	public long getBeginUploadTime() {
		return beginUploadTime;
	}

	public void setBeginUploadTime(long beginUploadTime) {
		this.beginUploadTime = beginUploadTime;
	}

	public List<XrayImageModel> getXrayImageModels() {
		return xrayImageModels;
	}

	public synchronized void setXrayImageModels(List<XrayImageModel> xrayImageModels) {
		this.xrayImageModels = xrayImageModels;
	}

	public long getGenerateTime() {
		return generateTime;
	}

	public void setGenerateTime(long generateTime) {
		this.generateTime = generateTime;
	}

	public void addXrayImageMode(XrayImageModel xrayImageModel) {
		synchronized (synObject) {
			xrayImageModels.add(xrayImageModel);	
		}
	}
	
	public void clearXrayImageMode() {
		synchronized (synObject) {
			this.generateTime = 0;
			xrayImageModels.clear();	
		}
	}

	@Override
	public String toString() {
		return "XrayImageComponent [xrayImageModels=" + xrayImageModels
				+ ", generateTime=" + generateTime + "]";
	}
}
