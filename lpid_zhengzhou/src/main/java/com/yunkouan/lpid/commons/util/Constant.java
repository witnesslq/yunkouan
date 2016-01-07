package com.yunkouan.lpid.commons.util;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;

/**
 * <P>Title: csc_sh</P>
 * <P>Description:系统常量类 </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月4日-上午11:36:42</P>
 * @author yangli
 * @version 1.0.0
 */
public class Constant {
	private static Logger logger = LoggerFactory.getLogger(Constant.class);
	//请求成功
	public final static String SUCCESS = "success";
	//请求失败
	public final static String FAILURE = "failure";
	// 项目名称
	public static final String WEBROOT = "lpid_zhengzhou";
    /**
     * The extension separator character.
     */
    public static final char EXTENSION_SEPARATOR = '.';

    /**
     * The Unix separator character.
     */
    public static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    public static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    public static final char SYSTEM_SEPARATOR = File.separatorChar;
    
    /**
     * 图片处理后存放路径
     */
    public static final String DEAL_DIRECTORY =  "xrayPhotos/deal/";
    
    /**
     * 图片处理前存放路径
     */
    public static final String NO_DEAL_DIRECTORY =  "xrayPhotos/nodeal/";
    /**
     * 云图引擎图片存放路径
     */
    public static final String CLOUD_DIRECTORY =  "xrayPhotos/cloud/";
    /**
     * 可见光图片存放路径
     */
    public static final String IMAGE_DIRECTORY =  "photos/";
    
    /**
     * 分屏A频道
     */
    public static final String CHANNEL_A = "A";
    
    /**
     * 分屏B频道
     */
    public static final String CHANNEL_B = "B";
    /**
     * 分屏B频道
     */
    public static final String CHANNEL_NUCLEAR = "nuclear";
    /**
     * PLC数据监控控制页面
     */
    public static final String CHANNEL_PLC = "plcChannel";
    
    /**
     * 远程查验频道
     */
    public static final String CHANNEL_REMOTECHECK = "remoteCheck";
    
    /**
     * 发现疑似违禁物品后，给PLC发送包裹编号后的恢复指令
     */
    public static int SEND_PACKAGENO_RESET = 0;

    /**
     * 核检测推送命令
     */
    public static int NUCLEAR_PUSH = 1;
    /**
     * PLC初始化状态值
     */
    public static int INIT_ITEM_VALUE = 1;
    /**
     * 查验模式
     */
    public static int AUTO_MODE_R = 0;
    /**
     * 快速通关模式
     */
    public static int QUICK_MODE_R = 1;
    /**
     * 分拣模式
     */
    public static int CHECK_MODE_R = 2;
    /**
     * 无模式
     */
    public static int NO_MODE_R = 3;
    /**
     * 全线停止
     */
    public static int STOP_MODE_R = 4;
    /**
     * 放行
     */
    public static final int DISCHARGED = 0;

    /**
     * 查验
     */
    public static final int INTERCEPT = 1;
    /**
     * 生产线上的包裹信息
     */
    public static Map<Integer, RunningPackageModel> RUNNING_PACKAGE = new ConcurrentHashMap<Integer, RunningPackageModel>();
    
    /**
     * 开启算法
     */
    public static String START_IMG_ALG = "1";
    
    /**
     * 关闭算法
     */
    public static int STOP_IMG_ALG = 0;
    
    /**
     * 人工点分拣按钮分拣
     */
    public static int MANUAL_CHECK = 0;
    
    /**
     * 根据算法分拣
     */
    public static int AUTO_CHECK =1;
    
    /**
     * 核推箱标志
     */
    public static int NUCLEARALARMPUSH = 1;
    
    public static final String CHARSET_NAME = "utf-8";
    
    /**
     * 入库
     */
	public static final short ENTRY_WAREHOUSE = 1;
    /**
     * 出库
     */
	public static final short EXIT_WAREHOUSE = 2;
	
    /**
     * @brief 消息类型
     * @author Administrator
     */
    public static class EDIMessageType{
    	public static final String SCA_MESSAGE = "SCA";		//快件过线数据
    	public static final String RSK_MESSAGE = "RSK";		//查验即决式布控数据
    	public static final String BIN_MESSAGE = "BIN";		//入仓记录
    	public static final String BOU_MESSAGE = "BOU";		//出仓记录
    }
    
    public static void removePackage(String packageNo){
    	int no = Integer.valueOf(packageNo);
    	logger.debug("移除前map的数量是:{}",Constant.RUNNING_PACKAGE.size());
    	Constant.RUNNING_PACKAGE.remove(no);
    	logger.debug("移除后map的数量是:{}",Constant.RUNNING_PACKAGE.size());
    }
}
