/**  
* @Title: ImageGun.java
* @Description:
* @Copyright: Copyright(c) 2015
* @author yangli 
* @date 2015年6月19日-上午10:11:45
* @version 1.0.0
*/ 
package com.yunkouan.lpid.biz.screenscompare.imageDealDll.bo;

import java.util.List;

import com.yunkouan.lpid.biz.screenscompare.bo.CoordModel;

public class ImageCoordData {
	private String imagename;//图片名称
	private int width;// 图片宽度
	private int height;// 图片高度
	private String objecttype;// 检测类型
	
	private List<CoordModel> coords; // 坐标数据
	
	public String getImagename() {
		return imagename;
	}
	public void setImagename(String imagename) {
		this.imagename = imagename;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getObjecttype() {
		return objecttype;
	}
	public void setObjecttype(String objecttype) {
		this.objecttype = objecttype;
	}
	public List<CoordModel> getCoords() {
		return coords;
	}
	public void setCoords(List<CoordModel> coords) {
		this.coords = coords;
	}
	@Override
	public String toString() {
		return "ImageCoordData [imagename=" + imagename + ", width=" + width + ", height=" + height + ", objecttype="
				+ objecttype + ", coords=" + coords + "]";
	}
}
