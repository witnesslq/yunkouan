package com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgalg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.ImageDrawRunnable;
import com.yunkou.common.util.ImageDrawRunnable.Coord;
import com.yunkou.common.util.ImageDrawRunnable.Image;
import com.yunkou.common.util.ThreadUtil;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.screenscompare.bo.CoordModel;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageModel;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.bo.ImageCoordData;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.bo.ResultData;
import com.yunkouan.lpid.biz.screenscompare.util.eh100.RfidNoReaderRunnable;
import com.yunkouan.lpid.biz.trace.bo.TraceTimeBo;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.commons.util.ResourceBundleUtil;
import com.yunkouan.lpid.modbus.service.SyncPLCService;
import com.yunkouan.lpid.persistence.entity.TraceTime;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: X光图片算法线程</P>
 * <p>调用算法结束,返回结果的json中坐标信息为空： 
 * 						表示没有危险品、违禁品，则保存包裹信息； 
 * 				返回结果的json中坐标信息不为空:
 * 						表示有危险品、违禁品，则给PLC发送贴标指令、启动RFID标签读卡器线程</p>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月10日-下午5:43:59</P>
 * 
 * @author yangli
 * @version 1.0.0
 */
public class ImageAlgThread implements Runnable {
	protected static Logger logger = LoggerFactory.getLogger(ImageAlgThread.class);

//	private static ScreenscompareService screenscompareService;
	private RunningPackageModel runningPackageModel;
	private String xrayAbsolutePaths;// X光图片绝对路径(d:/images/1.jpg;d:/images/2.jpg;d:/images/3.jpg)
	private long lastPushTime; // 上一次推包的时间
	private static final long PUSH_INTERVAL_TIME = ResourceBundleUtil.getSystemLong("PUSH_INTERVAL_TIME");// 两次推包的间隔时间
	private ExecutorService executorService = Executors.newCachedThreadPool();// 线程池
	private Gson gson ;
	private int plcId = ResourceBundleUtil.getModbusInt("modbus.write.package_no");
	private static final String IMAGEPATH = ResourceBundleUtil.getSystemString("IMAGEPATH");
	private TraceTimeBo traceTimeBo;
	private Map<String,TraceTime> traceTimes;
	public ImageAlgThread(RunningPackageModel runningPackageModel, String xrayAbsolutePaths) {
		this.runningPackageModel = runningPackageModel;
		this.xrayAbsolutePaths = xrayAbsolutePaths;
		this.gson = new Gson();
		this.traceTimeBo = SpringApplicationContext.getBean(TraceTimeBo.class);
		this.traceTimes = this.traceTimeBo.getTraceTimes();
//		if (screenscompareService == null) {
//			screenscompareService = SpringApplicationContext.getBean(ScreenscompareService.class);
//		}
	}

	@Override
	public void run() {
		TraceTime traceTime = this.traceTimes.get(runningPackageModel.getPackageNo());
		if(traceTime != null) traceTime.setAlgStartTime(System.currentTimeMillis());
		
		// 调用算法，返回json数据.
		logger.debug("三视角X光机,调用算法的图片路径：{}",xrayAbsolutePaths);
		String imageCoordJsonData = ImageAlgUtils.dealImage(xrayAbsolutePaths);
		logger.debug("三视角X光机,算法结束返回的json数据：{}",imageCoordJsonData);
		
		if(traceTime != null) traceTime.setAlgEndTime(System.currentTimeMillis());
		
		// 把json数据转换为对象
		ResultData resultData = null;
		try {
			resultData = gson.fromJson(imageCoordJsonData, ResultData.class);
		} catch (JsonSyntaxException e) {
			logger.error("行邮算法调用结束返回的json数据转换成对象失败！json:{}",imageCoordJsonData);
			imageCoordJsonData = getNewData(imageCoordJsonData);
			logger.error("新的json数据:{}",imageCoordJsonData);
			try {
				resultData = gson.fromJson(imageCoordJsonData, ResultData.class);
			} catch(Exception ex) {
				logger.error("行邮算法json数据转换成对象失败！json:{}",imageCoordJsonData);
			}
		}
		
		setImageCoordForRunningPackage(resultData);
	}
	
	/**
	 * 算法调用后,返回的json数据有问题,使用该方法进行容错处理.
	 * 		(截掉错误的json数据,留下正确的部分.这样就可以正确的转换为对象,进行后续处理.)
	 * @param json
	 * @return
	 */
	private static String getNewData(String json) {
		String newJson = "";
		int index = json.indexOf("suspect");
		int end = json.indexOf("}", index);
		newJson = json.substring(0, end + 1);
		return newJson;
	}
	
	/**
	 * 把调用算法结束后返回的json数据,设置给包裹对象.
	 */
	public void setImageCoordForRunningPackage(ResultData resultData) {
		List<ImageCoordData> imageCoordDatas = null;
		List<XrayImageModel> xrayImageModels = runningPackageModel.getXrayImages();
		
		if (xrayImageModels == null || xrayImageModels.size() == 0) {
			logger.error("包裹信息异常！！！包裹编号：{}", runningPackageModel.getPackageNo());
			Constant.RUNNING_PACKAGE.remove(Integer.parseInt(runningPackageModel.getPackageNo()));
			return;
		} else if(resultData == null) {
			logger.error("X光图片{}调用算法出错.",xrayAbsolutePaths);
			return ;
		} else if ((imageCoordDatas = resultData.getResult()) == null || imageCoordDatas.size() == 0) {
			logger.error("X光图片{}调用算法，没有坐标信息.",xrayAbsolutePaths);
			return;
		}

		int suspect = resultData.getSuspect();
		for (XrayImageModel xrayImageModel : xrayImageModels) { // 从包裹对象runningPackageModel中取得X光图片信息
			// 从runningPackageModel中取得X光图片名称,
			// 用来和算法调用完的json中的图片名称比较.
			String name = xrayImageModel.getImageName();
			List<CoordModel> coordForDraw = new ArrayList<CoordModel>();
			// 从算法返回结果中取得X光图片及坐标信息
			for (ImageCoordData imageCoordData : imageCoordDatas) { 
				String imageName = imageCoordData.getImagename();
				String objectType = imageCoordData.getObjecttype();
				List<CoordModel> coords = imageCoordData.getCoords();
				
				if (!name.equalsIgnoreCase(imageName))
					continue;

				xrayImageModel.setObjectType(objectType);
				xrayImageModel.setSuspect(suspect);
				
				// 4.1、没有坐标信息
				if (coords == null || coords.size() == 0) {
					logger.debug("4.1 ==========没有坐标信息>>>图片名称：{}", imageName);
					runningPackageModel.setLabelFlag(0);
				}
				// 4.2、有坐标信息
				else {
					logger.debug("4.2 ==========有坐标信息>>>图片名称：{}", imageName);
					// 4.2.1、给前端画面推送json坐标信息
//					String jsonXrayImage = gson.toJson(imageCoordDatas);
//					String resultJsonData = JsonUtil.transformJson("coord", jsonXrayImage);
//					sendCoordMessage(resultJsonData);
//					logger.info("推送给前端页面的json坐标数据：" + resultJsonData);
//					logger.debug("4.2.1 ==========给前端画面推送json坐标信息>>>包裹编号：{}", runningPackageModel.getPackageNo());

					// 5、给PLC发送贴标指令
					if((lastPushTime == 0) || (System.currentTimeMillis() - lastPushTime > PUSH_INTERVAL_TIME)) {
						lastPushTime = System.currentTimeMillis();
						pushPackage();
					}

					// 6、读取标签号码
					long rfidReadTimeout = ResourceBundleUtil.getSystemInt("RFID_READ_TIMEOUT");
					executorService.execute(new RfidNoReaderRunnable(runningPackageModel, rfidReadTimeout));
					
					coordForDraw.addAll(coords);
					// 取得坐标json数据
					runningPackageModel.setLabelFlag(1);
					xrayImageModel.setCoords(gson.toJson(coords));
					
					drawXrayImage(coordForDraw, xrayImageModel);
				}
			}
		}
	}
	
	/**
	 * X光图片画圈业务
	 * @throws IOException 
	 */
	private void drawXrayImage(List<CoordModel> coords, XrayImageModel xrayImageModel){
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

		File srcFile = new File(xrayImageModel.getAbsolutePath());
		File destFile = new File(IMAGEPATH + Constant.DEAL_DIRECTORY + DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_DATE_FORMAT), xrayImageModel.getImageName());
		
		try {
			org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
			drawRunnable.img.srcImage = destFile;
			
			// 启动X光图片的画圈线程
			executorService.execute(drawRunnable);
		} catch (IOException e) {
			logger.debug("拷贝图片出错",e);
		}
	}
	public static void main(String[] args) {
//		String imageCoordJsonData = ImageAlgUtils.dealImage("E:/151204_162946_00001739_1.jpg");
	}
	/**
	 * 给PLC发送推包指令
	 */
	// 推送包裹
	// 和流水线PLC设备的交互，对要贴标的物品，写入包裹编号
	private void pushPackage() {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				TraceTime traceTime = traceTimes.get(runningPackageModel.getPackageNo());
				if(traceTime != null) traceTime.setStartPushTime(System.currentTimeMillis());
				
				logger.debug("5.==========给PLC发送贴标指令>>>包裹编号：{}", runningPackageModel.getPackageNo());
				int packNo = Integer.valueOf(runningPackageModel.getPackageNo());
				SyncPLCService.getInstance().syncWritePlc(plcId, packNo);
				if(traceTime != null) traceTime.setEndPushTime(System.currentTimeMillis());
				
				// 休眠100毫秒时间，将贴标设备进行重置
				ThreadUtil.sleep(2000);
				logger.info("给PLC发送复位指令{},包裹编号:{}", Constant.SEND_PACKAGENO_RESET, runningPackageModel.getPackageNo());
				// 包裹编号清零
				SyncPLCService.getInstance().syncWritePlc(plcId, Constant.SEND_PACKAGENO_RESET);
			}
		});
	}
}
