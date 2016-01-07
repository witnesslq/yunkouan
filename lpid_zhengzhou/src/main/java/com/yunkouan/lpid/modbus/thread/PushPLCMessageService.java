package com.yunkouan.lpid.modbus.thread;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.serotonin.util.ThreadUtils;
import com.yunkou.common.util.ThreadUtil;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.commons.listener.CommonListener;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.modbus.model.MemoryModel;

/**
 * @author yangli
 * <p>推送plc数据</p>
 * 2015年11月27日 下午12:23:42
 */
public class PushPLCMessageService implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(CommonListener.class); 
	private MemoryModel memoryModel;
	private Gson gson ;
	private BroadcasterFactory broadcasterFactory ;
	public PushPLCMessageService(){
		gson = new Gson();
	}
	@Override
	public void run() {
		ThreadUtils.sleep(5000);
		if(broadcasterFactory ==null) broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
		if(memoryModel==null) memoryModel = SpringApplicationContext.getBean(MemoryModel.class);
		while(true) {
			String json = gson.toJson(memoryModel);
			//通过broadcasterFactory对象获取查验频道对象
		    Broadcaster broadcaster = broadcasterFactory.lookup(DefaultBroadcaster.class, Constant.CHANNEL_PLC, true);
		    //向查验频道的订阅者发送算法计算后的坐标
		    broadcaster.broadcast(json);
			ThreadUtil.sleep(500);
		}
	}
}
