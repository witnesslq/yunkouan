package com.yunkouan.lpid.commons.listener;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import com.serotonin.util.ThreadUtils;
import com.yunkou.common.util.ResourceBundleUtil;
import com.yunkouan.lpid.biz.screenscompare.util.eh100.RfidLinkRunnable;
import com.yunkouan.lpid.modbus.thread.PushPLCMessageService;
import com.yunkouan.lpid.modbus.thread.SyncPLCRunnable;
import com.yunkouan.lpid.rfid.service.LLRPRunnable;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 系统监听类</P>
 * <P>Copyimport com.yunkouan.lpid.module.opc.thread.OPCClientRunnable;
 * <p>Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年11月27日-下午6:31:21</P>
 * @author yangli
 * @version 1.0.0
 */
public class CommonListener extends ContextLoaderListener implements ServletContextListener {
	
	private static Logger logger = LoggerFactory.getLogger(CommonListener.class); 
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private LLRPRunnable LLRPRunnable1 ;
	private LLRPRunnable LLRPRunnable2 ;
	
	public void contextInitialized(ServletContextEvent event) {
//		// 启动连接读卡器线程
//		executorService.execute(new RfidLinkRunnable());
//		logger.info("启动连接读卡器线程成功！");
//		
//		// 启动监听PLC线程
//		executorService.execute(SyncPLCRunnable.getInstance().new SyncPLCReaderRunnable());
//		logger.info("启动监听PLC线程成功！");
//		
//		// 启动天线发卡器线程
//		LLRPRunnable1 = new LLRPRunnable(ResourceBundleUtil.getSystemString("SUBWAY_IP1"));
//		LLRPRunnable2 = new LLRPRunnable(ResourceBundleUtil.getSystemString("SUBWAY_IP2"));
//		executorService.execute(LLRPRunnable1);
//		executorService.execute(LLRPRunnable2);
//		logger.info("启动天线发卡器线程成功！");
//		
		// 推送PLC数据
		executorService.execute(new PushPLCMessageService());
		logger.info("所有监听启动成功!");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			RfidLinkRunnable.readrfidClient.close();
		} catch (IOException e) {
			logger.error("行邮系统关闭时，RFID读卡器连接线程销毁失败！");
		}
		
		try {
			LLRPRunnable1.stop();
			LLRPRunnable1.disconnect();
		} catch(Throwable e) {
			logger.error("行邮系统关闭时，天线发卡器线程销毁失败！IP:{}" , LLRPRunnable1.getIp());
		}
		try {
			LLRPRunnable2.stop();
			LLRPRunnable2.disconnect();
		} catch(Throwable e) {
			logger.error("行邮系统关闭时，天线发卡器线程销毁失败！IP:{}" , LLRPRunnable1.getIp());
		}
		
		
		logger.debug("行邮系统即将关闭......");
		super.contextDestroyed(event);
	}
	
	
}
