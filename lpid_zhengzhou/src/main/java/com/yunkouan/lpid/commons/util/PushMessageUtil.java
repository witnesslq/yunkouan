package com.yunkouan.lpid.commons.util;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;

@Component(value="pushMessageUtil")
public class PushMessageUtil {
	private Gson gson;
	private BroadcasterFactory broadcasterFactory;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public PushMessageUtil() {
		gson = new Gson();
		broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
	}

	public void pushMessage(Object message, String channel){
		if(message == null || channel == null) return ;
		String json = gson.toJson(message);
    	//通过broadcasterFactory对象获取查验频道对象
    	final Broadcaster broadcasterA = broadcasterFactory.lookup(DefaultBroadcaster.class, channel, true);
    	//向查验频道的订阅者发送算法计算后的坐标
    	broadcasterA.broadcast(json);
    	logger.debug("推送到画面的json数据:{}",json);
	}
}
