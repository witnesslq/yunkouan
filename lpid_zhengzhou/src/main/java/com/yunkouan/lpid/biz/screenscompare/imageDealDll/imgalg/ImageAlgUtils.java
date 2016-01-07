package com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgalg;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkou.common.util.ResourceBundleUtil;

/**
 * X光图片算法处理类(带画圈)
 * <P>Title: csc_sh</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年8月28日-上午10:14:58</P>
 * @author  yangli
 * @version 1.0.0
 */
public class ImageAlgUtils {
	private static final Logger logger = LoggerFactory.getLogger(ImageAlgUtils.class);
	private static Detect detect = null;
	static int result = 1;//初始化结果(0:正确;其他为错误)
	static String s ;
	private static final String OBJECT_TYPE = ResourceBundleUtil.getSystemString("OBJECT_TYPE");
	private static final String XRAY_TYPE = ResourceBundleUtil.getSystemString("XRAY_TYPE");
	private static Object synchObj = new Object();
	
	static {
		detect = Detect.INSTANCE;
		result = detect.DetectionInit(OBJECT_TYPE, XRAY_TYPE);
		s = Detect.filePath;
		logger.debug("filePath:{}", s);
	}

	/**
	 * 处理图片方法(计算坐标,返回json)
	 * 
	 * @param xrayImagePath x光图片的完整路径 c:/tomcat/cscsh/resource/xrayPhoto/nodeal/20121212/1.jpg
	 * @since 1.0.0
	 * 2014年8月28日-下午3:48:39
	 */
	public static String dealImage(String xrayImagePath) {
		synchronized (synchObj) {
			try {
				 String json = detect.Detection(xrayImagePath, 0);
				 return json ;
			} catch (Exception e) {
				logger.error("X光图片算法调用失败！",e);
				return null;
			}
		}
	}
	
	public static void main(String[] args) {
		String s = "D:/imageweb/xrayPhotos/nodeal";
//		getFile(s);
	}
	
	private void getFile(String path){
		File file = new File(path);
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f : files) {
				getFile(f.getAbsolutePath());
			}
		} else if(file.isFile()) {
			String xrayPath = file.getAbsolutePath();
			if(xrayPath.endsWith("_1.jpg")){
				logger.debug(xrayPath);
				dealImage(xrayPath);
			}
		}
	}
}
