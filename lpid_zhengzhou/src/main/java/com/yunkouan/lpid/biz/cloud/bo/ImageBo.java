package com.yunkouan.lpid.biz.cloud.bo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.yunkou.common.util.JsonUtil;
import com.yunkouan.lpid.biz.screenscompare.bo.CoordModel;

public class ImageBo {
	private String absolutePath;// 绝对路径
	private String relativePath;// 图片相对路径,用来在画面上显示
	private String imagename;//图片名称
	private int width;// 图片宽度
	private int height;// 图片高度
	private String objecttype;// 检测类型
//	private int suspect;// 疑似度
	
	private List<CoordModel> coords; // 坐标数据
	
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
//	public int getSuspect() {
//		return suspect;
//	}
//	public void setSuspect(int suspect) {
//		this.suspect = suspect;
//	}
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
		return "ImageBo [absolutePath=" + absolutePath + ", relativePath=" + relativePath + ", imagename=" + imagename
				+ ", width=" + width + ", height=" + height + ", objecttype=" + objecttype + ", coords=" + coords + "]";
	}
}
