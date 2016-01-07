/**  
* @Title: XrayImageModel.java
* @Description:
* @Copyright: Copyright(c) 2015
* @author  yangli 
* @date 2015年7月27日-上午10:29:34
* @version 1.0.0
*/ 
package com.yunkouan.lpid.biz.screenscompare.bo;

import java.util.Date;

/**
 * X光图片对应的Model类
 */
public class XrayImageModel {
	private String absolutePath; // X光图片绝对路径(d:/image/xxx.jpg)
	
	// 下面属性名称和entity类中属性名称相同
	private String imageName;// X光图片名称
	private String imageRelativePath;// X光图片相对存放路径
	private Integer width;// X光图片宽度
	private Integer height; // X光图片高度
	private Date generateTime;// 接收到X光图片时的时间
	private String objectType; // 检测类型
	private String coords; // 检测结果(坐标数据)
	private String viewType; // 视角类型
	private Integer suspect;// 疑似度

	private RunningPackageModel runningPackage;
	
//	private String imagename;//图片名称
//	private String objecttype;// 检测类型
	/**
	 * X光图片名称
	 * @return
	 */
	public String getImageName() {
		return imageName;
	}
	/**
	 * X光图片名称
	 * @param imageName
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	/**
	 * X光图片相对存放路径
	 * @return
	 */
	public String getImageRelativePath() {
		return imageRelativePath;
	}
	/**
	 * X光图片相对存放路径
	 * @param imageRelativePath
	 */
	public void setImageRelativePath(String imageRelativePath) {
		this.imageRelativePath = imageRelativePath;
	}
	/**
	 * X光图片绝对路径(d:/image/xxx.jpg)
	 * @return
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}
	/**
	 * X光图片绝对路径(d:/image/xxx.jpg)
	 * @param absolutePath
	 */
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	/**
	 * X光图片宽度
	 * @return
	 */
	public Integer getWidth() {
		return width;
	}
	/**
	 * X光图片宽度
	 * @param width
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}
	/**
	 * X光图片高度
	 * @return
	 */
	public Integer getHeight() {
		return height;
	}
	/**
	 * X光图片高度
	 * @param height
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}
	/**
	 * 接收到X光图片时的时间
	 * @return
	 */
	public Date getGenerateTime() {
		return generateTime;
	}
	/**
	 * 接收到X光图片时的时间
	 * @param generateTime
	 */
	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
	}
	/**
	 * 检测类型
	 * @return
	 */
	public String getObjectType() {
		return objectType;
	}
	/**
	 * 检测类型
	 * @param objectType
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	/**
	 * 检测结果(坐标数据)
	 * @return
	 */
	public String getCoords() {
		return coords;
	}
	/**
	 * 检测结果(坐标数据)
	 * @param coords
	 */
	public void setCoords(String coords) {
		this.coords = coords;
	}
	/**
	 * 包裹model对象
	 */
	public RunningPackageModel getRunningPackage() {
		return runningPackage;
	}
	/**
	 * 包裹model对象
	 * @param runningPackage
	 */
	public void setRunningPackage(RunningPackageModel runningPackage) {
		this.runningPackage = runningPackage;
	}
	/**
	 * 视角类型
	 * @return
	 */
	public String getViewType() {
		return viewType;
	}
	/**
	 * 视角类型
	 * @param viewType
	 */
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	/**
	 * 疑似度
	 * @return
	 */
	public Integer getSuspect() {
		return suspect;
	}
	/**
	 * 疑似度
	 * @param suspect
	 */
	public void setSuspect(Integer suspect) {
		this.suspect = suspect;
	}
	@Override
	public String toString() {
		return "XrayImageModel [absolutePath=" + absolutePath + ", imageName=" + imageName + ", imageRelativePath="
				+ imageRelativePath + ", width=" + width + ", height=" + height + ", generateTime=" + generateTime
				+ ", objectType=" + objectType + ", coords=" + coords + ", viewType=" + viewType + ", suspect="
				+ suspect + ", runningPackage=" + runningPackage + "]";
	}
}
