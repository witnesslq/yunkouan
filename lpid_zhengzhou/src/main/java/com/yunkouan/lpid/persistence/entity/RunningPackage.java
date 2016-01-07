package com.yunkouan.lpid.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageModel;


/**
 * The persistent class for the check_info database table.
 * 
 */
@Entity
@Table(name="running_package")
@NamedQuery(name="RunningPackage.findAll", query="SELECT c FROM RunningPackage c")
public class RunningPackage extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
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
	private List<XrayImage> xrayImages = new ArrayList<XrayImage>();

    public RunningPackage() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @detail RFID号码
	 * @return
	 */
	@Column(name="rfid_no",length = 50, nullable = true)
	public String getRfidNo() {
		return rfidNo;
	}
	/**
	 * @detail 
	 * @param rfidNo RFID号码
	 */
	public void setRfidNo(String rfidNo) {
		this.rfidNo = rfidNo;
	}
	/**
	 * @detail 可见光图片名称 
	 * @return
	 */
	@Column(name="photo_name",length = 20, nullable = true)
	public String getPhotoName() {
		return photoName;
	}
	/**
	 * @detail 
	 * @param photoName 可见光图片名称
	 */
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	/**
	 * @detail 可见光图片存放路径 
	 * @return
	 */
	@Column(name="photo_relative_path",length = 200, nullable = true)
	public String getPhotoRelativePath() {
		return photoRelativePath;
	}
	/**
	 * @detail 
	 * @param photoRelativePath 可见光图片存放路径
	 */
	public void setPhotoRelativePath(String photoRelativePath) {
		this.photoRelativePath = photoRelativePath;
	}
	/**
	 * @detail 可见光图片宽度 
	 * @return
	 */
	@Column(name="photo_width")
	public Integer getPhotoWidth() {
		return photoWidth;
	}
	/**
	 * @detail 
	 * @param photoWidth 可见光图片宽度
	 */
	public void setPhotoWidth(Integer photoWidth) {
		this.photoWidth = photoWidth;
	}
	/**
	 * @detail 可见光图片高度 
	 * @return
	 */
	@Column(name="photo_height")
	public Integer getPhotoHeight() {
		return photoHeight;
	}
	/**
	 * @detail 
	 * @param photoHeight 可见光图片高度 
	 */
	public void setPhotoHeight(Integer photoHeight) {
		this.photoHeight = photoHeight;
	}
	/**
	 * @detail 包裹宽度
	 * @return
	 */
	@Column(name="width")
	public Integer getWidth() {
		return this.width;
	}
	/**
	 * @detail 
	 * @param width 包裹宽度
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}
	/**
	 * @detail 包裹高度 
	 * @return
	 */
	@Column(name="height")
	public Integer getHeight() {
		return this.height;
	}
	/**
	 * @detail 
	 * @param height 包裹高度
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}
	/**
	 * @detail 操作人 
	 * @return
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="operator_id")
	public Operator getOperator() {
		return this.operator;
	}
	/**
	 * @detail 
	 * @param operator 操作人
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	/**
	 * @detail 操作时间 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	public Date getCreateTime() {
		return this.createTime;
	}
	/**
	 * @detail 
	 * @param createTime 操作时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @detail  
	 * @return 贴标表示(1:贴标;0:未贴标) 
	 */
	 @Column(name="label_flag")
	public Integer getLabelFlag() {
		return labelFlag;
	}
	/**
	 * @detail 
	 * @param labelFlag 贴标表示(1:贴标;0:未贴标) 
	 */
	public void setLabelFlag(Integer labelFlag) {
		this.labelFlag = labelFlag;
	}
	/**
	 * @detail X光图片信息
	 * @return
	 */
	@OneToMany(mappedBy="runningPackage",cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.EAGER)
	public List<XrayImage> getXrayImages() {
		return xrayImages;
	}
	/**
	 * @detail 
	 * @param xrayImages X光图片信息
	 */
	public void setXrayImages(List<XrayImage> xrayImages) {
		this.xrayImages = xrayImages;
	}
	@Override
	public String toString() {
		return "RunningPackage [id=" + id + ", rfidNo=" + rfidNo + ", width="
				+ width + ", height=" + height + ", operator=" + operator
				+ ", createTime=" + createTime + ", labelFlag=" + labelFlag
				+ ", photoName=" + photoName + ", photoRelativePath="
				+ photoRelativePath + ", photoWidth=" + photoWidth
				+ ", photoHeight=" + photoHeight + ", xrayImages=" + xrayImages
				+ "]";
	}
}