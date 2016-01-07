package com.yunkouan.lpid.rfid.service;

import java.util.Iterator;
import java.util.List;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.yunkou.common.util.JsonUtil;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageModel;
import com.yunkouan.lpid.biz.screenscompare.service.ScreensCompareHistoryService;

@Controller
@RequestMapping(value="/epc")
public class PushController {
	@Autowired
	private ScreensCompareHistoryService historyService;
	
	@RequestMapping(value = "/push")
	@ResponseBody
	public void push(String epc){
		RunningPackageModel runningPackageModel = historyService.getRunningPackageModelByEPC(epc);
		List<XrayImageModel> xrayImmageModels = runningPackageModel.getXrayImages();
		Iterator<XrayImageModel> it = xrayImmageModels.iterator();
		while(it.hasNext()) {
			XrayImageModel xrayImageModel = it.next();
			xrayImageModel.setRunningPackage(null);
		}
		String json = new Gson().toJson(runningPackageModel);
		String result = JsonUtil.transformJson("views", json);
		BroadcasterFactory broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
    	//通过broadcasterFactory对象获取查验频道对象
    	final Broadcaster broadcasterA = broadcasterFactory.lookup(DefaultBroadcaster.class, "A", true);
    	//向查验频道的订阅者发送算法计算后的坐标
    	broadcasterA.broadcast(result);
		
//		Set<String> channels = Constant.CHANNELS;
//		Iterator<String> it = channels.iterator();
//		while(it.hasNext()) {
//			String channel = it.next();
//			
//			BroadcasterFactory broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
//	    	//通过broadcasterFactory对象获取查验频道对象
//	    	final Broadcaster broadcasterA = broadcasterFactory.lookup(DefaultBroadcaster.class, channel, true);
//	    	//向查验频道的订阅者发送算法计算后的坐标
//	    	broadcasterA.broadcast(channel);
//		}
	}
}
