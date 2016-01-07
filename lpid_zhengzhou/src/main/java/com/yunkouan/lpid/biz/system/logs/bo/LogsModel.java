package com.yunkouan.lpid.biz.system.logs.bo;

import java.util.Date;

import com.yunkouan.lpid.persistence.entity.LogType;
import com.yunkouan.lpid.persistence.entity.Operator;


public class LogsModel {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private int id;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 日志类型
	 */
	private LogType logType;
	/**
	 * 操作员
	 */
	private Operator operator;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the logType
	 */
	public LogType getLogType() {
		return logType;
	}
	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param logType the logType to set
	 */
	public void setLogType(LogType logType) {
		this.logType = logType;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
}
