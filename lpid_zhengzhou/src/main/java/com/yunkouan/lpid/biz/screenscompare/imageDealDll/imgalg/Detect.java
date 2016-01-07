package com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgalg;

import java.io.File;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Detect extends Library {
	public String path = Detect.class.getResource("").getFile();
	/*
	 * 获取dll文件的绝对路径
	 * 
	 * File(path).getPath(),转换格式如：E:\eclipse\workspace...
	 * System.getProperties().getProperty("sun.arch.data.model") 判断当前使用的JRE是32位还是64位 
	 */
	String filePath = new File(path).getPath() + File.separator + "dll" + System.getProperties().getProperty("sun.arch.data.model") + File.separator + "ObjectDetect";
	
	Detect INSTANCE = (Detect) Native.loadLibrary(filePath , Detect.class);
	
	// 初始化
	int DetectionInit(String objectName,String DeviceName);
	
	// 检测算法
	String Detection(String imagesPath,int missErrorRatio);
	
	// 释放
	void DetectionUnInit();
}
