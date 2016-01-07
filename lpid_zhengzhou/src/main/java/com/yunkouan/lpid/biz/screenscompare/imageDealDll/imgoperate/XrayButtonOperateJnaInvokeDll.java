package com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgoperate;

import java.io.File;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 图片处理JNA调用接口</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月3日-下午6:59:08</P>
 * @author yangli
 * @version 1.0.0
 */
public interface XrayButtonOperateJnaInvokeDll extends Library {
    //获取当前绝对路径，格式如/E:/eclipse/workspace...
	public String path = XrayButtonOperateJnaInvokeDll.class.getResource("").getFile();
	/*
	 * 获取dll文件的绝对路径
	 * 
	 * File(path).getPath(),转换格式如：E:\eclipse\workspace...
	 * System.getProperties().getProperty("sun.arch.data.model") 判断当前使用的JRE是32位还是64位 
	 */
	String filePath = new File(path).getPath() + File.separator + 
            "dll" + System.getProperties().getProperty("sun.arch.data.model") + File.separator + "XrayButtonOperate";
	//加载dll文件
	XrayButtonOperateJnaInvokeDll INSTANCE = (XrayButtonOperateJnaInvokeDll) Native.loadLibrary(filePath, XrayButtonOperateJnaInvokeDll.class);

	void ColorFineTune(String imageIn,int method,String imageOut);
    
}
