package com.yunkouan.lpid.biz.screenscompare.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.FileUtils;
import com.yunkou.common.util.JsonUtil;
import com.yunkou.common.util.ResourceBundleUtil;
import com.yunkou.common.util.ThreadUtil;
import com.yunkou.common.util.TwoTuple;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageComponent;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageModel;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgalg.ImageAlgThread;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgalg.ImageAlgUtils;
import com.yunkouan.lpid.biz.trace.bo.TraceTimeBo;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.persistence.entity.RunningPackage;
import com.yunkouan.lpid.persistence.entity.TraceTime;
import com.yunkouan.lpid.persistence.entity.XrayImage;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 接收3视角X光机发送过来的图片(3张图片)</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年8月27日-下午4:23:38</P>
 * 
 * @author yangli
 * @version 1.0.0
 */
@Controller
@RequestMapping("/xmachine")
public class XrayImageController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired private XrayImageComponent component;
	// 图片存放根目录(D:/imageweb/)
	private static final String IMAGE_PATH= ResourceBundleUtil.getSystemString("IMAGEPATH");
	// X光图片存放根目录(D:/imageweb/xrayPhotos/nodeal/)
	private static final String NO_DEAL_ROOT_PATH = IMAGE_PATH + Constant.NO_DEAL_DIRECTORY; 
	// 当前日期 (格式:20150724)
	private static String today; 
	// X光图片名称(1.jpg)
	private String xrayImageName; 
	// 上次开始上传时间
	private static long lastTakeTime = 0;
	@Autowired
	private TraceTimeBo traceTimeBo;
	private Map<String,TraceTime> traceTimes;
	/**
	 * <p> 接收X光图片 </p>
	 * 
	 * @param req
	 * @param res
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @since 1.0.0 2014年8月27日-下午4:24:01
	 */
	@RequestMapping(value = "/uploadFiles")
	@ResponseBody
	public void uploadFile(HttpServletRequest request) throws IllegalStateException, IOException {
		logger.debug("uploadFile start:{}" , System.currentTimeMillis());
		if(Constant.RUNNING_PACKAGE.size() == 0){
			logger.error("没有包裹信息,不接收X光图片.");
			return ;
		}
		
		this.component.setBeginUploadTime(System.currentTimeMillis());
		
		// 取得上传过来的文件数组,可能包含任何格式文件.
		List<MultipartFile> multipartFiles = FileUtils.getMultipartFileList(request);

		// 判断是否有上传文件
		if (multipartFiles == null || multipartFiles.size() == 0) {
			logger.debug("没有上传文件！");
			return;
		}

		// 上传X光图片到本地，并保存起来
		List<File> xrayImageFiles = transferToForXrayImage(multipartFiles,new ArrayList<File>());
		
		// 设置包裹(XrayImageModel)对象
		setXrayImageModel(xrayImageFiles);
		
		// 调用算法，返回json数据. 
//		String imageCoordJsonData = ImageAlgUtils.dealImage(xrayImageFiles.get(0).getAbsolutePath());
		logger.debug("uploadFile end:{}" , System.currentTimeMillis());
	}

	/**
	 * @detail 上传X光图片到本地，并保存起来
	 * @param multipartFiles
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	private List<File> transferToForXrayImage(List<MultipartFile> multipartFiles,List<File> xrayImageFiles) throws IllegalStateException, IOException {
		// 遍历List集合，取出每一个X光图片
		for (MultipartFile multipartFile : multipartFiles) {
			// 上传图片到本地
			File xrayImage = generateXrayImageFile(multipartFile);
			if(xrayImage == null) continue;
			String imageName = xrayImage.getName();
			if(!imageName.endsWith("_gz.jpg")){
				multipartFile.transferTo(xrayImage);// 上传
				logger.debug("X光图片{}上传成功！",xrayImage.getName());
				// 忽略原子序数文件
				xrayImageFiles.add(xrayImage);
			}
		}
		return xrayImageFiles;
	}

	/**
	 * 生成X光图片的文件
	 */
	private File generateXrayImageFile(MultipartFile multipartFile) {
		// 创建存放X光图片的文件夹
		generateXrayFolder();

		// 取得图片名称
		xrayImageName = multipartFile.getOriginalFilename();
		if (StringUtils.isBlank(xrayImageName) || xrayImageName.endsWith("_gz.jpg")) return null;
		
		// 存放X光图片文件
		File xrayFile = new File(NO_DEAL_ROOT_PATH + today, xrayImageName);

		if (!xrayFile.exists()) {
			try {
				xrayFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("创建X光图片文件失败！", e);
			}
		}

		return xrayFile;
	}

	/**
	 * 创建存放X光图片的文件夹
	 */
	private void generateXrayFolder() {
		// 取得当前日期
		today = DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_DATE_FORMAT);
		File folder = new File(NO_DEAL_ROOT_PATH + today);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	/**
	 * 设置包裹(XrayImageModel)对象
	 * 
	 * @param xrayImageFile
	 */
	private void setXrayImageModel(List<File> xrayImageFiles) {
		component.clearXrayImageMode();
		
		for (File xrayImageFile : xrayImageFiles) {
			// 计算X光图片的大小(宽度、高度)
			TwoTuple<Integer, Integer> xraySize = FileUtils.calcImageWidthAndHeight(xrayImageFile);

			String imageName = xrayImageFile.getName();
			XrayImageModel xrayImageModel = new XrayImageModel();
			xrayImageModel.setAbsolutePath(xrayImageFile.getAbsolutePath());
			xrayImageModel.setImageName(xrayImageFile.getName());
			xrayImageModel.setImageRelativePath(Constant.NO_DEAL_DIRECTORY + today + "/" + imageName);
			if(xraySize != null) xrayImageModel.setWidth(xraySize.first);
			if(xraySize != null) xrayImageModel.setHeight(xraySize.second);
			xrayImageModel.setGenerateTime(new Date(System.currentTimeMillis()));
			if(imageName.endsWith("_1.jpg")) {
				xrayImageModel.setViewType("1");
			} else if(imageName.endsWith("_2.jpg")) {
				xrayImageModel.setViewType("2");
			} else if(imageName.endsWith("_3.jpg")) {
				xrayImageModel.setViewType("3");
			}
			component.getXrayImageModels().add(xrayImageModel);
		}
		logger.debug("设置X光图片的生成时间:{}", System.currentTimeMillis());
		component.setGenerateTime(System.currentTimeMillis());
	}
	
//	public void imgalg(RunningPackageModel runningPackageModel, String xrayImagePaths){
//    	Thread t = new Thread(new ImageAlgThread(runningPackageModel, xrayImagePaths));
//    	t.start();
//    }
}
