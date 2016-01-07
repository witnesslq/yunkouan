/**  
* @Title: ResultData.java
* @Description:
* @Copyright: Copyright(c) 2015
* @author  yangli 
* @date 2015年6月5日-上午11:17:43
* @version 1.0.0
*/ 
package com.yunkouan.lpid.commons.model;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class ResultData implements Serializable{
	/**
	 * 状态(-1:失败;0:成功)
	 */
	private String status;
	/**
	 * 消息
	 */
	private String message;
	/**
	 * 结果
	 */
	private Object result;
	/**
	 * @detail 状态(-1:失败;0:成功) 
	 * @return
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status 状态(-1:失败;0:成功)
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @detail 消息 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message 消息
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @detail 结果
	 * @return
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * @param result 结果
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "ResultData [status=" + status + ", message=" + message
				+ ", result=" + result + "]";
	}
}
