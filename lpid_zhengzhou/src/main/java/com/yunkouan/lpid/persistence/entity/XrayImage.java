/**  
* @Title: XrayImage.java
* @Description:
* @Copyright: Copyright(c) 2015
* @author yangli 
* @date 2015年7月25日-上午11:45:44
* @version 1.0.0
*/ 
package com.yunkouan.lpid.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.common.eventbus.AllowConcurrentEvents;

/**
 * @author Administrator
 *
 */
@Entity
@Table(name="xray_image")
@NamedQuery(name="XrayImage.findAll", query="SELECT c FROM XrayImage c")
public class XrayImage  extends AbstractEntity {
	private Integer id;
	private String imageName;// 图片名称
	private String imageRelativePath;// X光图片相对存放路径
	private Integer width;// 宽度
	private Integer height; // 高度
	private Date generateTime;// 接收到X光图片时的时间
	private String objectType; // 检测类型
	private String coords; // 检测结果(坐标数据)
	private String viewType; // 视角类型
	private Integer suspect;// 疑似度
	private RunningPackage runningPackage;
	/**
	 * 
	 */
	public XrayImage() {
	}
	/**
	 * @param objectType
	 * @param imagename
	 * @param width
	 * @param height
	 */
	public XrayImage(String objectType, String imageName, Integer width,
			Integer height) {
		super();
		this.imageName = imageName;
		this.width = width;
		this.height = height;
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
	@Column(name="image_name")
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	@Column(name="width")
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	@Column(name = "height")
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	@Column(name = "object_type")
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	@Column(name = "coords")
	public String getCoords() {
		return coords;
	}
	public void setCoords(String coords) {
		this.coords = coords;
	}
	@ManyToOne
	@JoinColumn(name="running_package")
	public RunningPackage getRunningPackage() {
		return runningPackage;
	}
	public void setRunningPackage(RunningPackage runningPackage) {
		this.runningPackage = runningPackage;
	}
	@Column(name="image_relative_path")
	public String getImageRelativePath() {
		return imageRelativePath;
	}
	public void setImageRelativePath(String imageRelativePath) {
		this.imageRelativePath = imageRelativePath;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="generate_time")
	public Date getGenerateTime() {
		return generateTime;
	}
	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
	}
	@Column(name = "view_type")
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	@Column(name="suspect")
	public Integer getSuspect() {
		return suspect;
	}
	public void setSuspect(Integer suspect) {
		this.suspect = suspect;
	}
}
