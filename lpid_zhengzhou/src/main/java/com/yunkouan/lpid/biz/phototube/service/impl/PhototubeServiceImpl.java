package com.yunkouan.lpid.biz.phototube.service.impl;


import static com.yunkou.common.util.DateTimeUtil.ISO_DATE_TIME_FORMAT;
import static com.yunkou.common.util.DateTimeUtil.getDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.yunkou.common.util.BeanMapper;
//import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.JsonUtil;
import com.yunkou.common.util.ResourceBundleUtil;
import com.yunkou.common.util.ThreadUtil;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.phototube.service.PhototubeService;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageComponent;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageModel;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgalg.ImageAlgThread;
import com.yunkouan.lpid.biz.screenscompare.util.HK886ServerRunnable;
import com.yunkouan.lpid.biz.screenscompare.util.eh100.RfidNoReaderRunnable;
import com.yunkouan.lpid.biz.trace.bo.TraceTimeBo;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.modbus.model.PhototubeUseageEnum;
import com.yunkouan.lpid.persistence.entity.TraceTime;

/**
 * <P>Title: lpid_zhengzhou</P>
 * <P>Description: 处理光电管触发的信号</P>
 * 业务主流程:
 * 	1.生成包裹对象。
 * 	2.设置包裹长度。
 * 	3.监听X光图片。
 * 	4.调用算法。
 * 	5.给PLC发送贴标指令。
 * 	6.读取RFID标签的EPC号码。
 * 	7.可见光拍照
 * <P>Copyright: Copyright(c) 2015</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2015年05月28日-01时06分36秒</P>
 * @author yangli
 * @version 1.0.0
 */
@Service("phototubeService")
public class PhototubeServiceImpl implements PhototubeService {
	protected static Logger logger = LoggerFactory.getLogger(PhototubeServiceImpl.class);
	@Autowired
	private XrayImageComponent component;
	@Autowired
	private TraceTimeBo traceTimeBo;
	private Map<String,TraceTime> traceTimes;
	//线程池
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    
	private Map<Integer, RunningPackageModel> runningPackageModelMap = Constant.RUNNING_PACKAGE;
	
	private int packageNo = 0; // 当前的包裹编号
	
	/**
	 * @param phototubeSerialNo PLC变量地址
	 * @param phototubeValue PLC变量地址对应的值
	 */
	@Override
	public void handle(PhototubeUseageEnum phototubeSerialNo , int phototubeValue) {
		traceTimes = traceTimeBo.getTraceTimes();
		
		switch (phototubeSerialNo) {
		case READ_PAGGAGE_NO:
			logger.debug("1.==========创建包裹对象>>>包裹通过X光机，读取到的包裹编号:" + phototubeValue);
			updateRunningPackageModelMap(phototubeValue);
			break;
		case PAGGAGE_LENGTH:
			logger.debug("2.==========设置包裹长度>>>包裹通过X光机，读取到的包裹长度:" + phototubeValue);
			setRunningPackageModeWidth(phototubeValue);
			break;
		default:
			logger.error("handle error... ...phototubeValue:" + phototubeValue + "; phototubeSerialNo:" + phototubeSerialNo);
		}
	}
   
   	/**
	 * 1、创建包裹对象
	 *    
	 * @param phototubeNo 包裹编号
	 */
	private void updateRunningPackageModelMap(int phototubeNo) {
		this.packageNo = phototubeNo;
		// 生成新的包裹对象
		RunningPackageModel runningPackageModel = new RunningPackageModel();
		runningPackageModel.setPackageNo(String.valueOf(packageNo));
		runningPackageModel.setGenerateTime(System.currentTimeMillis());
		runningPackageModelMap.put(phototubeNo, runningPackageModel);
		logger.debug("生成包裹的时间:{}",runningPackageModel.getGenerateTime());
		
		TraceTime traceTime = new TraceTime();
		traceTime.setPaggageInitTime(runningPackageModel.getGenerateTime());
		traceTimes.put(String.valueOf(packageNo), traceTime);
		// 创建包裹对象后，就启动线程，等待一段时间后就对包裹的外形进行拍照留存。
		takePhoto(runningPackageModel);
	}
	
	/**
	 * 2、设置包裹长度
	 * @param packageWidth 包裹长度
	 */
	private boolean setRunningPackageModeWidth(int packageWidth){
		RunningPackageModel runningPackageModel = runningPackageModelMap.get(packageNo);
		if(runningPackageModel == null) {
			logger.error("设置包裹长度时，没有找到编号为" + packageNo + "的包裹");
    		return false;
    	} 
		runningPackageModel.setWidth(packageWidth);
		// 开始监听X光图片
		executorService.execute(new XrayImageListener(packageNo));
		return true;
	}
    
    /**
     * 3、设置X光图片信息
     */
    private class XrayImageListener implements Runnable {
    	private int phototubeMappingPackageNo;//包裹编号
    	private long xrayImageTimeout = ResourceBundleUtil.getSystemLong("XRAY_IMAGE_TIME");
    	private RunningPackageModel runningPackageModel = null;
    	
    	public XrayImageListener(int phototubeMappingPackageNo) {
			this.phototubeMappingPackageNo = phototubeMappingPackageNo;
			runningPackageModel = runningPackageModelMap.get(phototubeMappingPackageNo);
		}
    	
		@Override
		public void run() {
			logger.info("3.==========开始监听X光图片产生>>>");
			if(runningPackageModel == null) {
				logger.info("监听X光图片产生时，没有找到包裹对象。包裹编号：{}", phototubeMappingPackageNo);
				return ;
			}
			
			long packageGenerateTime = runningPackageModel.getGenerateTime();
			logger.debug("开始监听X光图片信息,包裹对象生成的时间：{},光电管编号：{}", getDateTime(packageGenerateTime,ISO_DATE_TIME_FORMAT) , phototubeMappingPackageNo);
			//等待X光图片产生
			while(true) {
				// 1. 判断是否超时：
				//    已经超时(当前时间 - 包裹生成时间 > 大于包裹超时时间)，则退出；
				//    否则，继续执行。
				if(System.currentTimeMillis() - packageGenerateTime > xrayImageTimeout) {
                    logger.error("超时情况下,当前时间为:{}",System.currentTimeMillis());
                    // 超时情况，清空MAP中对应包裹编号的对象。
                    runningPackageModelMap.remove(this.phototubeMappingPackageNo);
					break; 
				}
				
				// 2. 判断是否有X光图片产生：
				//    generateTime = 0 表示没有生成X光图片，休眠后继续执行下次循环; 
				//    否则，表示生成了X光图片。
				long generateTime = component.getGenerateTime();
				
				// 3. 判断X光图片是否是当前包裹对应的图片：
				//    X光图片产生时间必须要大于包裹对象产生的时间(包裹对象先创建,X光图片后创建.);
				//    否则，继续下次循环。
				if(generateTime > packageGenerateTime) {
					TraceTime traceTime = traceTimes.get(runningPackageModel.getPackageNo());					
					if(traceTime != null) {
						traceTime.setXrayStartTime(component.getBeginUploadTime());
						traceTime.setXrayEndTime(generateTime);
					}
					logger.debug("接收完X光图片,当前时间:{}",System.currentTimeMillis());
					logger.debug("X光图片产生的时间：{} - 包裹生成的时间:{} = {}毫秒", new Object[]{generateTime, packageGenerateTime, (generateTime-packageGenerateTime)});
					logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^找到X光图片");
					
					// 拷贝X光图片List
					List<XrayImageModel> xrayImageModelListSrc = component.getXrayImageModels();
					List<XrayImageModel> xrayImageModelListDest = new ArrayList<XrayImageModel>();//BeanMapper.mapList(xrayImageModelsSrc, XrayImageModel.class);

					if(xrayImageModelListSrc == null || xrayImageModelListSrc.size() <= 0) {
					    logger.error("等待X光图片时候，获取xrayImageModels为null");
					    break;
					}else {
						
					}
					//下面这行代码的错误信息如下：
					//Exception in thread "pool-5-thread-4" java.util.ConcurrentModificationException
					for(XrayImageModel xrayImageModel: xrayImageModelListSrc) {
						// X光图片产生的时间 > 包裹产生的时间  (正常情况)
						if(xrayImageModel.getGenerateTime().getTime() > runningPackageModel.getGenerateTime()) {
							XrayImageModel ximl = BeanMapper.map(xrayImageModel, XrayImageModel.class);
							xrayImageModelListDest.add(ximl);	
						} else {// X光图片产生的时间 < 包裹产生的时间  (异常情况)
							xrayImageModelListSrc.remove(xrayImageModel);
						}
					}
					
					if(xrayImageModelListDest == null || xrayImageModelListDest.size() == 0) {
						logger.error("X光图片信息异常,退出当前执行操作。包裹编号{}", runningPackageModel.getPackageNo() );
						component.clearXrayImageMode();
						// 异常情况，清空MAP中对应包裹编号的对象。
	                    runningPackageModelMap.remove(this.phototubeMappingPackageNo);
	                    return ;
					} else {
						
					}
					
					logger.debug("设置X光图片信息>>>包裹编号:{}", runningPackageModel.getPackageNo());
					logger.debug("设置X光图片信息:" + xrayImageModelListDest.toString());
					// 设置X光图片List(双向关联)
					runningPackageModel.setXrayImages(xrayImageModelListDest);
					
					// 把图片信息转换为json形式，推送到前端画面。
					// 在这个位置推送数据，是为了防止runningPackageModel中的xrayImages集合和包裹对象的双向关联，这样json转换时会出错。
//					RunningPackageModel runningPackageModelJson = BeanMapper.map(runningPackageModel, RunningPackageModel.class);
//			    	pushXrayImageMessage(runningPackageModelJson);
					
					for(XrayImageModel xrayImageModel: xrayImageModelListDest) {
						xrayImageModel.setRunningPackage(runningPackageModel);
					}
					
					component.clearXrayImageMode();
					
					// 4、调用算法
//			    	String xrayImagePaths = getThreeXrayImagesPath(xrayImageModelListDest);
			    	String xrayImagePath = getXrayImagePath(xrayImageModelListDest);
			    	imgalg(runningPackageModel, xrayImagePath);
			    	
			    	break;
				} else {
//					logger.debug("休眠10毫秒，继续监听X光图片");
					ThreadUtil.sleep(10);
					continue;
				}
				
//				break;
			}
		}
    }
    
    /**
     * 4、调用算法
     */
    public void imgalg(RunningPackageModel runningPackageModel, String xrayImagePaths){
    	logger.debug("4.==========调用算法>>>包裹编号：{}",runningPackageModel.getPackageNo());
    	executorService.execute(new ImageAlgThread(runningPackageModel, xrayImagePaths));
    }
    
    /**
     * 5.给PLC发送贴标指令。[该功能放在算法调用完成后]
     */
//    private static void sendPLCCommand(RunningPackageModel runningPackageModel){
//    	executorService.execute(new Runnable() {
//			@Override
//			public void run() {
//				
//			}
//		});
//    }
    
    /**
     * 6.读取RFID标签的EPC号码。
     */
    private void readRFID(RunningPackageModel runningPackageModel){
    	int rfidReadTimeout = ResourceBundleUtil.getSystemInt("RFID_READ_TIMEOUT");
    	executorService.execute(new RfidNoReaderRunnable(runningPackageModel,rfidReadTimeout));
    }
   
    /**
     * x、可见光拍照(时序不定)
     */
    private void takePhoto(RunningPackageModel runningPackageModel) {
    	executorService.execute(new HK886ServerRunnable(runningPackageModel));
    }
    
    /**
     * 取得调用算法X光图片的绝对路径
     * @param xrayImageModels
     * @return
     */
    private String getXrayImagePath(List<XrayImageModel> xrayImageModels){
    	for(XrayImageModel xrayImageModel : xrayImageModels) {
    		String absolutePath = xrayImageModel.getAbsolutePath();
    		if(absolutePath.endsWith("_1.jpg")) {
    			return absolutePath;
    		}
    	}
    	return "";
    }
    
    /**
     * 拼接3张图片的绝对路径，以";"分割
     * 
     * @param runningPackageModel
     * @return 
     * @since 1.0.0
     * 2015年7月31日-下午6:38:38
     */
    private String getThreeXrayImagesPath(List<XrayImageModel> xrayImageModels){
    	if(xrayImageModels == null) return null;
    	
    	StringBuffer xrayImagesPath = new StringBuffer();
    	for(XrayImageModel xrayImageModel : xrayImageModels) {
    		xrayImagesPath.append(xrayImageModel.getAbsolutePath());
    		xrayImagesPath.append(";");
    	}
    	
    	if(!StringUtils.isEmpty(xrayImagesPath.toString())){
    		xrayImagesPath.deleteCharAt(xrayImagesPath.toString().length()-1);
    	}
    	
    	return xrayImagesPath.toString();
    }
    
    /**
     * 把X光图片信息推送到前端
     * @param xrayImageModelsDest
     */
    private void pushXrayImageMessage(final RunningPackageModel runningPackageModel){
    	executorService.execute(new Runnable() {
			@Override
			public void run() {
				String json = new Gson().toJson(runningPackageModel);
				String jsonVal = JsonUtil.transformJson("image", json);
				logger.debug("推送给前端画面的X光图片的json数据：" + jsonVal);
				
				BroadcasterFactory broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
		    	//通过broadcasterFactory对象获取查验频道对象
		    	final Broadcaster broadcasterA = broadcasterFactory.lookup(DefaultBroadcaster.class, Constant.CHANNEL_A, true);
		    	//通过broadcasterFactory对象获取查验频道对象
		    	final Broadcaster broadcasterB = broadcasterFactory.lookup(DefaultBroadcaster.class, Constant.CHANNEL_B, true);
		    	//向查验频道的订阅者发送算法计算后的坐标
		    	broadcasterA.broadcast(jsonVal);
		    	broadcasterB.broadcast(jsonVal);
			}
		});
    }    
}
