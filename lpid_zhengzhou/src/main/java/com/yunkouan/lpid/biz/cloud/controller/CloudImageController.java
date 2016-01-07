package com.yunkouan.lpid.biz.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.FileUtils;
import com.yunkou.common.util.TwoTuple;
import com.yunkouan.lpid.biz.cloud.bo.CloudImageResult;
import com.yunkouan.lpid.biz.cloud.bo.ImageBo;
import com.yunkouan.lpid.biz.cloud.service.CloudAlgThread;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.commons.util.PushMessageUtil;
import com.yunkouan.lpid.commons.util.ResourceBundleUtil;

/**
 * 云图引擎控制器(仅限单视角X光机)
 * 
 * @author YangLi
 */
@Controller
@RequestMapping("/cloud")
public class CloudImageController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	// 路径1 X光图片存放根目录(E:/svn_new/lpid/lpid_zhengzhou/image/)
	private String rootPath = ResourceBundleUtil.getSystemString("IMAGEPATH") + Constant.CLOUD_DIRECTORY;
	// 路径2
	private String datePath; // 图片存放三级目录(根据保存时的当前日期生成)(/20150724)
	private String xrayImageName; // X光图片名称(1.jpg)
	private ExecutorService executorService = Executors.newCachedThreadPool();// 线程池对象
	private ImageBo imageBo;
	private static long lastUploadTime;// 上次上传图片时间
	@Autowired
	private PushMessageUtil pushMessageUtil;
	
	@RequestMapping(value = "/uploadFiles")
	@ResponseBody
	public void uploadFiles(HttpServletRequest request,String channel) throws IllegalStateException, IOException {
		if(System.currentTimeMillis() - lastUploadTime < 1000 && lastUploadTime > 0) {
			logger.debug("云图引擎,上次接收X光图片和本次接收时间间隔小于1000毫秒，拒绝接收图片.");
			return ;
		}
		
		imageBo = new ImageBo();
		
		// 取得上传过来的文件数组,可能包含任何格式文件.
		List<MultipartFile> multipartFiles = FileUtils.getMultipartFileList(request);

		// 判断是否有上传文件
		if (multipartFiles == null || multipartFiles.size() == 0) {
			logger.debug("没有上传文件！");
			return;
		}

		// 上传X光图片到本地，并保存起来
		List<File> images = transferToForXrayImage(multipartFiles, new ArrayList<File>());
		
		setImageBo(images);
		
		CloudImageResult cloudImageResult = new CloudImageResult();
		cloudImageResult.setType("cloud");
		cloudImageResult.setValue(imageBo);
		
		// 推送X光图片到画面
//		pushMessageUtil.pushMessage(cloudImageResult, channel);
		// 调用算法,推送坐标(发现违禁品才推送,否则不推送数据)
		cloudAlg(cloudImageResult,channel);
	}
	
	private void cloudAlg(CloudImageResult cloudImageResult, String channel) {
		executorService.execute(new CloudAlgThread(cloudImageResult,channel));
	}

	/**
	 * @detail 上传X光图片到本地，并保存起来
	 * @param multipartFiles
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	private List<File> transferToForXrayImage(List<MultipartFile> multipartFiles, List<File> xrayImageFiles)
			throws IllegalStateException, IOException {
		// 遍历List集合，取出每一个X光图片
		for (MultipartFile multipartFile : multipartFiles) {
			// 上传图片到本地
			File xrayImage = generateXrayImageFile(multipartFile);
			multipartFile.transferTo(xrayImage);// 上传
			xrayImageFiles.add(xrayImage);
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
		if (StringUtils.isBlank(xrayImageName))
			return null;
		// 存放X光图片文件
		File xrayFile = new File(rootPath + datePath, xrayImageName);

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
		datePath = DateTimeUtil.getCurrentDate(DateTimeUtil.YMD_DATE_FORMAT);
		File folder = new File(rootPath + datePath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}
	
	/**
	 * 设置ImageBo对象
	 */
	private void setImageBo(List<File> images){
		String absolutePath = images.get(0).getAbsolutePath();
		String imageName = images.get(0).getName();
		this.imageBo.setAbsolutePath(absolutePath);
		this.imageBo.setRelativePath(Constant.CLOUD_DIRECTORY + datePath + "/" + imageName);
		this.imageBo.setImagename(imageName);
		TwoTuple<Integer, Integer> sizeTuple = FileUtils.calcImageWidthAndHeight(images.get(0));
		if(sizeTuple != null) this.imageBo.setWidth(sizeTuple.first);
		if(sizeTuple != null) this.imageBo.setHeight(sizeTuple.second);
		this.imageBo.setCoords(null);
	}
}
