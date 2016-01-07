package com.yunkouan.lpid.biz.screenscompare.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.NativeLong;
import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.FileUtils;
import com.yunkou.common.util.ResourceBundleUtil;
import com.yunkou.common.util.ThreadUtil;
import com.yunkou.common.util.TwoTuple;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.service.ScreenscompareService;
import com.yunkouan.lpid.biz.screenscompare.util.hcnet.dll32.HCNetSDK;
import com.yunkouan.lpid.biz.trace.bo.TraceTimeBo;
import com.yunkouan.lpid.biz.trace.service.TraceService;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.persistence.entity.TraceTime;

/**
 * 日夜型网络枪型摄像机拍照模块
 * 
 * @author 杨荔
 * @DateTime 2014-4-8 下午4:56:23
 * @描述：
 */
public class HK886ServerRunnable implements Runnable{
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ScreenscompareService screenscompareService;
	private RunningPackageModel runningPackageModel;
	public static HCNetSDK hcsdk;
	public static NativeLong lUserID;
	private long light_NO = ResourceBundleUtil.getSystemLong("light_NO");// 通道号
	private String sDVRIP = ResourceBundleUtil.getSystemString("sDVRIP");// 设备IP地址或是静态域名，字符数不大于128个
	private short wDVRPort = (short) ResourceBundleUtil.getSystemInt("wDVRPort");// 设备端口号
	private String sUserName = ResourceBundleUtil.getSystemString("sUserName");// 登录的用户名
	private String sPassword = ResourceBundleUtil.getSystemString("sPassword");// 用户密码
	private String rootPath = ResourceBundleUtil.getSystemString("IMAGEPATH");// 拍照图片存放根路径
	private String imageRelative = Constant.IMAGE_DIRECTORY;// 拍照图片存放相对路径
	private TraceTimeBo traceTimeBo;
	private Map<String,TraceTime> traceTimes;
	private TraceService traceService;
	public HK886ServerRunnable(RunningPackageModel runningPackageModel) {
		this.runningPackageModel = runningPackageModel;
		this.screenscompareService = SpringApplicationContext.getBean(ScreenscompareService.class);
		this.traceTimeBo = SpringApplicationContext.getBean(TraceTimeBo.class);
		this.traceService = SpringApplicationContext.getBean(TraceService.class);
		this.traceTimes = this.traceTimeBo.getTraceTimes();
	}
	/**
	 * 可见光拍照
	 */
	public void takePhoto() {
		hcsdk = HCNetSDK.INSTANCE;
		// 1.设备初始化
		while (!hcsdk.NET_DVR_Init());

		// 设置连接时间与重连时间
		hcsdk.NET_DVR_SetConnectTime(2000, 1);
		hcsdk.NET_DVR_SetReconnect(10000, true);
		HCNetSDK.NET_DVR_DEVICEINFO_V30 lpDeviceInfo30 = new HCNetSDK.NET_DVR_DEVICEINFO_V30();// 设备参数结构体。
		HCNetSDK.NET_DVR_DEVICEINFO lpDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO();// 设备信息

		// 2.用户注册设备
		hcsdk.NET_DVR_Login_V30(sDVRIP, wDVRPort, sUserName, sPassword, lpDeviceInfo30);
		lUserID = hcsdk.NET_DVR_Login_V30(sDVRIP, wDVRPort, sUserName, sPassword, lpDeviceInfo30);
		logger.debug("lUserID:" + lUserID);
		int lastErr = hcsdk.NET_DVR_GetLastError();
		logger.debug("lastError:" + lastErr);

		// 通道号
		NativeLong lChannel = new NativeLong(light_NO);
		// logger.debug("lChannel:" + lChannel);

		// JPEG图像参数
		HCNetSDK.NET_DVR_JPEGPARA lpJpegPara = new HCNetSDK.NET_DVR_JPEGPARA();
		lpJpegPara.wPicSize = 6;
		lpJpegPara.wPicQuality = 0;/* 图片质量系数 0-最好 1-较好 2-一般 */

		File photoFile = getImageFile();
		boolean rtnValue = hcsdk.NET_DVR_CaptureJPEGPicture(lUserID, lChannel, lpJpegPara, photoFile.getAbsolutePath());
		
		if(rtnValue) {
			logger.debug("============可见光拍照成功！>>>图片名称:{}包裹编号：{}",photoFile.getName(),runningPackageModel.getPackageNo());
			// 设置可见光图片信息
			runningPackageModel.setPhotoName(photoFile.getName());
			runningPackageModel.setPhotoRelativePath(imageRelative +  DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_DATE_FORMAT) + "/" + photoFile.getName());
			TwoTuple<Integer,Integer> tuple = FileUtils.calcImageWidthAndHeight(photoFile.getAbsoluteFile());
			if(tuple != null) runningPackageModel.setPhotoWidth(tuple.first);//width
			if(tuple != null) runningPackageModel.setPhotoHeight(tuple.second);//height
			logger.debug("拍照后runningPackageModel对象的状态:{}", runningPackageModel.toString());
		} else {
			logger.debug("============可见光拍照失败！>>>错误码：{}",hcsdk.NET_DVR_GetLastError());
		}
		
		// 如果没有X光图片信息，不保存包裹信息
		runningPackageModel.setCreateTime(new Date());
		screenscompareService.saveRunningPackage(runningPackageModel);
		TraceTime traceTime = this.traceTimes.get(runningPackageModel.getPackageNo());
		Constant.removePackage(runningPackageModel.getPackageNo());
		if(traceTime != null){
			// 保存跟踪时间表
			this.traceService.saveTraceTime(traceTime);	
			this.traceTimes.remove(traceTime);
		} else {
			logger.debug("TraceTime is Null");
		}
	}

	@Override
	public void run() {
	    // 可见光开始拍照超时时间(单位毫秒):从X光机里的光电管发送信号开始计时
        long takePhotoLimitedTime = ResourceBundleUtil.getSystemLong("TAKE_PHOTO_LIMITED_TIME");
        long generateTime = runningPackageModel.getGenerateTime();
        //等待拍照
        while (true) {
            if(System.currentTimeMillis() - generateTime > takePhotoLimitedTime) break;
            ThreadUtil.sleep(100);
            continue;
        }
        
        logger.debug("7.==========可见光拍照>>>包裹编号：{}" , runningPackageModel.getPackageNo());
        if(runningPackageModel.getXrayImages() == null || runningPackageModel.getXrayImages().size() ==0) {
        	logger.info("当前对象没有X光图片,不进行拍照!");
        	Constant.removePackage(runningPackageModel.getPackageNo());
        	return;
        }
        TraceTime traceTime = this.traceTimes.get(runningPackageModel.getPackageNo());
        if(traceTime != null) traceTime.setTakePhotoStartTime(System.currentTimeMillis());
		takePhoto();
		if(traceTime != null) traceTime.setTakePhotoEndTime(System.currentTimeMillis());
	}
	
	/**
	 * @detail 取得可见光图片File
	 * @return
	 */
	private File getImageFile(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date nowDate = new Date(System.currentTimeMillis());
		String imageName = formatter.format(nowDate) + ".jpg"; // 可见光图片名称(1.jpg)
		String folder = rootPath + imageRelative +  DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_DATE_FORMAT) + "/"; // 存放可见光图片的文件夹(c:/123/456/)
		String imageAllPathName = folder + imageName ;// 图片路径全名称c:/123/1.jpg
		
		// 取得存放可见光图片的文件夹
		File fileFolder = new File(folder);
		if(!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
		
		// 取得可见光图片文件
		File file = new File(imageAllPathName);
		if(!file.exists()) {
		    try {
		        file.createNewFile();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
		return file;
	}
	
	public static void main(String[] args) {
		new Thread(new HK886ServerRunnable(new RunningPackageModel())).start();
	}
}
