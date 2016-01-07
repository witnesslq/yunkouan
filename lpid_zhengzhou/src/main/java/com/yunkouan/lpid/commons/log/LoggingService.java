package com.yunkouan.lpid.commons.log;

import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yunkou.common.util.DateTimeUtil;

@Aspect
@Component
public class LoggingService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Pointcut("execution (* com.yunkouan.lpid.biz.screenscompare.controller.XrayImageController.uploadFile(..))")
	public void upload() {
	}
	
	@Before(value="upload()")
	public void beforeUpload(JoinPoint joinPoint){
		long startUploadTime = new Date().getTime();
		logger.debug("开始接收x光图片.....时间：{}" , DateTimeUtil.getDateTime(startUploadTime,DateTimeUtil.ISO_DATE_TIME_FORMAT));
	}
	
	@After(value="upload()")
	public void afterUpload(){
		long endUploadTime = new Date().getTime();
		logger.debug("结束接收x光图片.....时间：{}" , DateTimeUtil.getDateTime(endUploadTime,DateTimeUtil.ISO_DATE_TIME_FORMAT));
//		logger.debug("总共上传共花费时间：{}毫秒", (endUploadTime - startUploadTime));
	}
}
