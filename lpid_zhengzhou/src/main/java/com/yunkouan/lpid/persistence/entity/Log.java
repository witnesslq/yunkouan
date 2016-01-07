package com.yunkouan.lpid.persistence.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the log database table.
 * 
 */
@Entity
@Table(name="log")
@NamedQuery(name="Log.findAll", query="SELECT l FROM Log l")
public class Log extends AbstractEntity  {
	private static final long serialVersionUID = 1L;
	private int id;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 日志内容
	 */
	private String content;
	/**
	 * 日志类型
	 */
	private LogType logType;
	/**
	 * 操作员
	 */
	private Operator operator;

	public Log() {
	}


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(length=2000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	//bi-directional many-to-one association to LogType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id")
	public LogType getLogType() {
		return this.logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}


	//bi-directional many-to-one association to Operator
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="operator_id")
	public Operator getOperator() {
		return this.operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

}