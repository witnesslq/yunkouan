package com.yunkouan.lpid.biz.screenscompare.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.yunkou.common.util.ParamsUtil;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.cloud.bo.CloudImageResult;
import com.yunkouan.lpid.biz.generic.controller.GenericController;
import com.yunkouan.lpid.biz.label.service.LabelService;
import com.yunkouan.lpid.biz.label.service.impl.LabelServiceImpl;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageModel;
import com.yunkouan.lpid.biz.screenscompare.service.ScreensCompareHistoryService;
import com.yunkouan.lpid.biz.system.config.service.ConfigService;
import com.yunkouan.lpid.biz.system.logs.bo.LogTypeCode;
import com.yunkouan.lpid.biz.system.logs.service.LogsService;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.modbus.service.SyncPLCService;
import com.yunkouan.lpid.persistence.entity.Label;
import com.yunkouan.lpid.persistence.entity.Operator;
import com.yunkouan.lpid.persistence.entity.ParamConfig;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月01日-04时29分21秒</P>
 * @author yangli
 * @version 1.0.0
 */
@Controller
@RequestMapping("/screenscompare")
public class ScreenscompareController extends GenericController {
    private final static String filePath = "/screenscompare";
    
    @Autowired
    private ScreensCompareHistoryService historyService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private LogsService logsService;
    
    /**
     * 实施查验画面(显示3视角和云图引擎画面)
     * 
     * @param request
     * @param response
     * @return 
     * @since 1.0.0
     * 2014年12月15日-下午1:35:24
     */
	@RequestMapping(value = "/compareshow")
    public String compareshow(HttpServletRequest request, HttpServletResponse response) {
		// 查询疑似度
		List<ParamConfig> params = configService.findConfigs();
		String suspect = "";
		for(ParamConfig param : params) {
			String name = param.getName();
			if(name.equalsIgnoreCase("suspect")) {
				suspect = param.getValue();
				break;
			}
		}
		request.setAttribute("suspect", suspect);
		
		// 查询标签
		Label label = labelService.findLabel();
		request.setAttribute("label", label);
		
		return filePath + "/compareshow";
    }
	
	@RequestMapping("/addLogs")
	@ResponseBody
	public void addLogs(HttpServletRequest request, String screenType, String imageSrc){
		// 添加日志
    	Operator opetrator = (Operator) request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
    	String desc = "";
    	if(screenType.equalsIgnoreCase("cloud")) {
    		desc = "[智能查验]云图引擎_图片路径：" + imageSrc   ;
    	} else if(screenType.equalsIgnoreCase("views")) {
    		desc =  "[智能查验]行邮查验_图片路径：" + imageSrc   ;
    	}
		
		LogTypeCode type = LogTypeCode.SCREEN_COMPARE;
    	logsService.insertLog(opetrator, type, desc);
	}
	
	// TODO
	@RequestMapping("/testQuery")
	@ResponseBody
	public RunningPackageModel testQuery(String epcNo){
		RunningPackageModel runningPackageModel = historyService.getRunningPackageModelByEPC(epcNo);
		for(XrayImageModel xrayImageModel : runningPackageModel.getXrayImages()){
			xrayImageModel.setRunningPackage(null);
		}
		return runningPackageModel;
	}
	
	// TODO
	@RequestMapping("/testChange")
	@ResponseBody
	public void testChange(Integer address, Integer plcValue){
		SyncPLCService.getInstance().syncWritePlc(address, plcValue);
	}
	
	@RequestMapping("/label")
	@ResponseBody
	/**
	 * 推送标签数量
	 */
	private void pushLabelMessage() {
		LabelService labelService = SpringApplicationContext.getBean(LabelServiceImpl.class);
		// 检索标签信息
		Label label = labelService.findLabel();
		// 更新标签信息(剩余标签数量 = 剩余标签数量  -1)
		label.setBalance(label.getBalance() - 1);
		labelService.updateBalance(label);
		// 推送标签
		CloudImageResult cir = new CloudImageResult();
		cir.setValue(label);
		cir.setType("label");
		Gson gson = new Gson();
		String json = gson.toJson(cir);
		
		BroadcasterFactory broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
    	//通过broadcasterFactory对象获取查验频道对象
    	Broadcaster broadcaster = broadcasterFactory.lookup(DefaultBroadcaster.class, Constant.CHANNEL_A, true);
    	//向查验频道的订阅者发送算法计算后的坐标
    	broadcaster.broadcast(json);
	}
}