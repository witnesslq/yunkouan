/**  
* @Title: ScreenscompareQueryModel.java
* @Description:
* @Copyright: Copyright(c) 2015
* @author yangli 
* @date 2015年8月20日-下午5:28:17
* @version 1.0.0
*/ 
package com.yunkouan.lpid.biz.screenscompare.bo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 包裹信息查询参数封装工具类
 * @author yangli
 */
public class ScreenscompareQueryModel {
//	@DateTimeFormat(pattern="yyyy-MM-dd")
//	private Date startDate;//开始日期
//	@DateTimeFormat(pattern="yyyy-MM-dd")
//	private Date endDate;//结束日期
//	
//	public Date getStartDate() {
//		return startDate;
//	}
//	public void setStartDate(Date startDate) {
//		this.startDate = startDate;
//	}
//	public Date getEndDate() {
//		return endDate;
//	}
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
	// 2015-06-23 06:20:11
	private String startDate;
	private String endDate;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "ScreenscompareQueryModel [startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}
}
