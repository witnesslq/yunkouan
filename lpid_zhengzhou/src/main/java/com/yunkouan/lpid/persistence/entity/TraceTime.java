package com.yunkouan.lpid.persistence.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="trace_time")
@NamedQuery(name="TraceTime.findAll", query="SELECT t FROM TraceTime t")
@Access(AccessType.PROPERTY)
public class TraceTime  extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**
	 * 行李生成时间
	 */
	private Long paggageInitTime = 0L;
	/**
	 * 开始接受x光图片的时间
	 */
	private Long xrayStartTime = 0L;
	/**
	 * 结束接受x光图片的时间
	 */
	private Long xrayEndTime = 0L;
	/**
	 * 开始调用算法的时间
	 */
	private Long algStartTime = 0L;
	/**
	 * 结束调用算法的时间
	 */
	private Long algEndTime = 0L;
	/**
	 * 给PLC发送贴标指令前的时间
	 */
	private Long startPushTime = 0L;
	/**
	 * 给PLC发送贴标指令后的时间
	 */
	private Long endPushTime = 0L;
	/**
	 * 可见光拍照开始的时间
	 */
	private Long takePhotoStartTime = 0L;
	/**
	 * 可见光拍照结束的时间
	 */
	private Long takePhotoEndTime = 0L;
	/**
	 * 开始监听EPC号码的时间
	 */
	private Long readEpcStartTime = 0L;
	/**
	 * 结束EPC号码的时间
	 */
	private Long readEpcEndTime = 0L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getPaggageInitTime() {
		return paggageInitTime;
	}
	public void setPaggageInitTime(Long paggageInitTime) {
		this.paggageInitTime = paggageInitTime;
	}
	public Long getXrayStartTime() {
		return xrayStartTime;
	}
	public void setXrayStartTime(Long xrayStartTime) {
		this.xrayStartTime = xrayStartTime;
	}
	public Long getXrayEndTime() {
		return xrayEndTime;
	}
	public void setXrayEndTime(Long xrayEndTime) {
		this.xrayEndTime = xrayEndTime;
	}
	public Long getAlgStartTime() {
		return algStartTime;
	}
	public void setAlgStartTime(Long algStartTime) {
		this.algStartTime = algStartTime;
	}
	public Long getAlgEndTime() {
		return algEndTime;
	}
	public void setAlgEndTime(Long algEndTime) {
		this.algEndTime = algEndTime;
	}
	public Long getStartPushTime() {
		return startPushTime;
	}
	public void setStartPushTime(Long startPushTime) {
		this.startPushTime = startPushTime;
	}
	public Long getEndPushTime() {
		return endPushTime;
	}
	public void setEndPushTime(Long endPushTime) {
		this.endPushTime = endPushTime;
	}
	public Long getTakePhotoStartTime() {
		return takePhotoStartTime;
	}
	public void setTakePhotoStartTime(Long takePhotoStartTime) {
		this.takePhotoStartTime = takePhotoStartTime;
	}
	public Long getTakePhotoEndTime() {
		return takePhotoEndTime;
	}
	public void setTakePhotoEndTime(Long takePhotoEndTime) {
		this.takePhotoEndTime = takePhotoEndTime;
	}
	public Long getReadEpcStartTime() {
		return readEpcStartTime;
	}
	public void setReadEpcStartTime(Long readEpcStartTime) {
		this.readEpcStartTime = readEpcStartTime;
	}
	public Long getReadEpcEndTime() {
		return readEpcEndTime;
	}
	public void setReadEpcEndTime(Long readEpcEndTime) {
		this.readEpcEndTime = readEpcEndTime;
	}
	
	
}
