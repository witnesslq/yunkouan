/**  
 * @Title: RunningPackageModel.java
 * @Description:
 * @Copyright: Copyright(c) 2015
 * @author  yangli 
 * @date 2015年7月24日-下午5:32:18
 * @version 1.0.0
 */
package com.yunkouan.lpid.biz.screenscompare.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yunkouan.lpid.persistence.entity.Operator;
import com.yunkouan.lpid.persistence.entity.XrayImage;

/**
 * 包裹信息Model类
 */
public class RunningPackageModel {
	private String packageNo;// 包裹编号(用来和PLC进行交互)
	private long generateTime;// 包裹对象创建时间(PLC给上位机发送信息，表示有新的包裹产生的时间)
	
	// 和Entity类对应的属性如下
	// ==========包裹信息==========
	private String rfidNo;// RFID号码
	private Integer width;// 包裹宽度
	private Integer height;// 包裹高度
	private Operator operator; // 操作人
	private Date createTime; // 操作时间
	private Integer labelFlag; // 贴标表示(1:贴标;0:未贴标) 
	
	// ==========可见光图片信息==========
	private String photoName;// 可见光图片名称
	private String photoRelativePath;// 可见光图片存放路径
	private Integer photoWidth;// 可见光图片宽度
	private Integer photoHeight;// 可见光图片高度
	
	// ==========X光图片信息==========
	private List<XrayImageModel> xrayImages = new ArrayList<XrayImageModel>();
	
	/**
	 * @return 包裹编号(用来和PLC进行交互)
	 */
	public String getPackageNo() {
		return packageNo;
	}
	/**
	 * @param packageNo 包裹编号(用来和PLC进行交互)
	 */
	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}
	/**
	 * @return 包裹对象创建时间(PLC给上位机发送信息，表示有新的包裹产生的时间)
	 */
	public long getGenerateTime() {
		return generateTime;
	}
	/**
	 * @param generateTime 包裹对象创建时间(PLC给上位机发送信息，表示有新的包裹产生的时间)
	 */
	public void setGenerateTime(long generateTime) {
		this.generateTime = generateTime;
	}
	/**
	 * @return RFID号码
	 */
	public String getRfidNo() {
		return rfidNo;
	}
	/**
	 * @param rfidNo RFID号码
	 */
	public void setRfidNo(String rfidNo) {
		this.rfidNo = rfidNo;
	}
	/**
	 * @return 包裹宽度
	 */
	public Integer getWidth() {
		return width;
	}
	/**
	 * @param width 包裹宽度
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}
	/**
	 * @return 包裹高度
	 */
	public Integer getHeight() {
		return height;
	}
	/**
	 * @param height 包裹高度
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}
	/**
	 * @return 操作人
	 */
	public Operator getOperator() {
		return operator;
	}
	/**
	 * @param operator 操作人
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	/**
	 * @return 操作时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime 操作时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return 贴标表示(1:贴标;0:未贴标) 
	 */
	public Integer getLabelFlag() {
		return labelFlag;
	}
	/**
	 * @param labelFlag 贴标表示(1:贴标;0:未贴标) 
	 */
	public void setLabelFlag(Integer labelFlag) {
		this.labelFlag = labelFlag;
	}
	/**
	 * @return 可见光图片名称
	 */
	public String getPhotoName() {
		return photoName;
	}
	/**
	 * @param photoName 可见光图片名称
	 */
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	/**
	 * @return 可见光图片存放路径
	 */
	public String getPhotoRelativePath() {
		return photoRelativePath;
	}
	/**
	 * @param photoRelativePath 可见光图片存放路径
	 */
	public void setPhotoRelativePath(String photoRelativePath) {
		this.photoRelativePath = photoRelativePath;
	}
	/**
	 * @return 可见光图片宽度
	 */
	public Integer getPhotoWidth() {
		return photoWidth;
	}
	/**
	 * @param photoWidth 可见光图片宽度
	 */
	public void setPhotoWidth(Integer photoWidth) {
		this.photoWidth = photoWidth;
	}
	/**
	 * @return 可见光图片高度
	 */
	public Integer getPhotoHeight() {
		return photoHeight;
	}
	/**
	 * @param photoHeight 可见光图片高度
	 */
	public void setPhotoHeight(Integer photoHeight) {
		this.photoHeight = photoHeight;
	}
	/**
	 * @return X光图片信息
	 */
	public List<XrayImageModel> getXrayImages() {
		return xrayImages;
	}
	/**
	 * @param xrayImages X光图片信息
	 */
	public void setXrayImages(List<XrayImageModel> xrayImages) {
		this.xrayImages = xrayImages;
	}
	@Override
	public String toString() {
		return "RunningPackageModel [packageNo=" + packageNo + ", generateTime=" + generateTime + ", rfidNo=" + rfidNo
				+ ", width=" + width + ", height=" + height + ", operator=" + operator + ", createTime=" + createTime
				+ ", labelFlag=" + labelFlag + ", photoName=" + photoName + ", photoRelativePath=" + photoRelativePath
				+ ", photoWidth=" + photoWidth + ", photoHeight=" + photoHeight + "]";
	}
}
