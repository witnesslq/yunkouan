package com.yunkouan.lpid.biz.cloud.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.ImageDrawRunnable;
import com.yunkou.common.util.ImageDrawRunnable.Coord;
import com.yunkou.common.util.ImageDrawRunnable.Image;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.cloud.bo.CloudImageResult;
import com.yunkouan.lpid.biz.cloud.bo.ImageBo;
import com.yunkouan.lpid.biz.screenscompare.bo.CoordModel;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.bo.ImageCoordData;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.bo.ResultData;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgalg.ImageAlgUtils;
import com.yunkouan.lpid.biz.system.config.service.ConfigService;
import com.yunkouan.lpid.biz.system.config.service.impl.ConfigServiceImpl;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.commons.util.PushMessageUtil;
import com.yunkouan.lpid.commons.util.ResourceBundleUtil;
import com.yunkouan.lpid.persistence.entity.ParamConfig;

public class CloudAlgThread implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(CloudAlgThread.class);
	private CloudImageResult cloudImageResult;
	private PushMessageUtil pushMessageUtil;
	private String channel;
	private static final String IMAGEPATH = ResourceBundleUtil.getSystemString("IMAGEPATH");
	private ExecutorService executorService = Executors.newCachedThreadPool();// 线程池
	private ConfigService configService;
	public CloudAlgThread(CloudImageResult cloudImageResult,String channel) {
		this.cloudImageResult = cloudImageResult;
		this.channel = channel;
		if(pushMessageUtil == null) pushMessageUtil = SpringApplicationContext.getBean(PushMessageUtil.class);
		if(configService == null) configService = SpringApplicationContext.getBean(ConfigServiceImpl.class);
	}
	
	@Override
	public void run() {
		// 系统设置的疑似度值
		int suspect = 0;
		ParamConfig suspectConfig = configService.findConfig("suspect");
		if(suspectConfig != null) suspect = Integer.parseInt(suspectConfig.getValue());
		
		ImageBo imageBo = (ImageBo) this.cloudImageResult.getValue();
		logger.debug("云图引擎,调用算法的图片:{}",imageBo.getAbsolutePath());
		String json = ImageAlgUtils.dealImage(imageBo.getAbsolutePath());
		logger.debug("云图引擎,调用后的json数据:{}",json);
		ResultData resultData = null;
		try {
			resultData = new Gson().fromJson(json, ResultData.class);
		} catch(Exception e) {
			logger.error("云图引擎算法调用后的json数据转换成对象失败！json:{}",json);
			return ;
		}
		if(resultData == null || resultData.getResult() == null || resultData.getResult().size()==0) return ;
		List<ImageCoordData> imageCoordDatas = resultData.getResult();
		ImageCoordData imageCoordData = imageCoordDatas.get(0);
		int currentSuspect = resultData.getSuspect();// 当前疑似度值
		imageBo.setObjecttype(imageCoordData.getObjecttype());
		imageBo.setCoords(imageCoordData.getCoords());
		List<CoordModel> coords = imageCoordData.getCoords();
		if(coords != null && coords.size() > 0) {
			// 采用下面两种方式都可以
//			pushMessageUtil.pushMessage(cloudImageResult, channel);
			if(currentSuspect >= suspect) {
				new Thread(new PushThread()).start();
			}
			
			String imageName = imageBo.getImagename();
			String newName = imageName.split("\\.")[0] + "_c.jpg";
			drawXrayImage(coords,imageBo.getAbsolutePath(),newName);
		}
	}
	
	/**
	 * X光图片画圈业务
	 * @throws IOException 
	 */
	private void drawXrayImage(List<CoordModel> coords, String absolutePath, String imageName){
		// 一个X光图片调用一次画圈线程
		ImageDrawRunnable drawRunnable = new ImageDrawRunnable();
		Image img = drawRunnable.new Image();
		drawRunnable.img = img;
		
		List<Coord> crds = new ArrayList<Coord>();
		for (CoordModel coordModel : coords) {
			Coord crd = drawRunnable.new Coord();
			crd.setX1(coordModel.getX1());
			crd.setX2(coordModel.getX2());
			crd.setX3(coordModel.getX3());
			crd.setX4(coordModel.getX4());
			crd.setY1(coordModel.getY1());
			crd.setY2(coordModel.getY2());
			crd.setY3(coordModel.getY3());
			crd.setY4(coordModel.getY4());
			crds.add(crd);
		}
		drawRunnable.img.coords.put(null, crds);

		File srcFile = new File(absolutePath);
		File destFile = new File(IMAGEPATH + Constant.CLOUD_DIRECTORY_DEAL + DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_DATE_FORMAT), imageName);
		
		try {
			org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
			drawRunnable.img.srcImage = destFile;
			
			// 启动X光图片的画圈线程
			executorService.execute(drawRunnable);
		} catch (IOException e) {
			logger.debug("拷贝图片出错",e);
		}
	}
	
	class PushThread implements Runnable{
		private PushThread(){}

		@Override
		public void run() {
			Gson gson = new Gson();
			BroadcasterFactory broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
			if(cloudImageResult == null || channel == null) return ;
			String json = gson.toJson(cloudImageResult);
	    	//通过broadcasterFactory对象获取查验频道对象
	    	final Broadcaster broadcasterA = broadcasterFactory.lookup(DefaultBroadcaster.class, channel, true);
	    	//向查验频道的订阅者发送算法计算后的坐标
	    	broadcasterA.broadcast(json);
	    	logger.debug("推送到画面的json数据:{}",json);			
		}
		
		
	}
}
