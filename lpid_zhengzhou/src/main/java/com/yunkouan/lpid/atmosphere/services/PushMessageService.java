/*
 * Copyright 2014 Jeanfrancois Arcand
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/**
 * This code was donated by Dan Vulpe https://github.com/dvulpe/atmosphere-ws-pubsub
 */
package com.yunkouan.lpid.atmosphere.services;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.BroadcasterLifeCyclePolicyListener;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.yunkou.common.util.JsonUtil;
import com.yunkou.common.util.ThreadUtil;
import com.yunkouan.lpid.atmosphere.dto.Command;
//import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.commons.util.Constant;

@Service
public class PushMessageService {
    @Autowired
    private BroadcasterFactory broadcasterFactory;
    private final static Logger LOG = LoggerFactory.getLogger(PushMessageService.class);
    private Map<String, Thread> runningPublishers = Maps.newConcurrentMap();
    //上一次广播的频道
	private static String lastRunner;
    //用于推包
//    private RunningPackageModel runningPackageModel;
    
    public void execute(Command command) {
        Broadcaster broadcaster = broadcasterFactory.lookup(DefaultBroadcaster.class, command.getChannel(), true);
        command.execute(broadcaster);
        //屏蔽远程查验频道
        if (!broadcaster.isDestroyed() && !isRunningThreadOnChannel(command.getChannel()) && !Constant.CHANNEL_REMOTECHECK.equals(command.getChannel())) {
            startMessagingThread(command.getChannel(), broadcaster);
        }
    }

    private synchronized boolean isRunningThreadOnChannel(String channel) {
        return runningPublishers.containsKey(channel) && runningPublishers.get(channel).isAlive();
    }

    private synchronized void startMessagingThread(String channel, Broadcaster broadcaster) {
    	try {
    		Thread thread = null;
    		// 如果不存在频道号，则不推送信息
    		if(StringUtils.isBlank(channel)) {
    			return ;
    		}
    		if(Constant.CHANNEL_NUCLEAR.equalsIgnoreCase(channel)){
//    			thread = new Thread(new NuclearChannelPublisher(broadcaster, channel));
    		} else {
    			thread = new Thread(new ChannelPublisher(broadcaster, channel));
    		}
    		thread.start();
    		runningPublishers.put(channel, thread);
		} catch (Exception e) {
			LOG.error("初始化ChannelPublisher失败", e);
		}
    }

    /**
     * <P>Title: ppoiq</P>
     * <P>Description: x光图片推送</P>
     * <P>Copyright: Copyright(c) 2014</P>
     * <P>Company: yunkouan.com</P>
     * <P>Date:2014年12月16日-上午10:57:25</P>
     * @author yangli
     * @version 1.0.0
     */
    public class ChannelPublisher implements Runnable, BroadcasterLifeCyclePolicyListener {
        private final Broadcaster broadcaster;
        private boolean shouldRun = true;
        private final String channel;

        public ChannelPublisher(Broadcaster broadcaster, String channel) {
            this.broadcaster = broadcaster;
            this.channel = channel;
            broadcaster.addBroadcasterLifeCyclePolicyListener(this);
        }

        @Override
        public void run() {
            while (shouldRun) {
            	//上次推送的频道
            	String lastRunner = getLastRunner();
            	//上次推送的频道和本次频道相同
            	if(this.channel.equals(lastRunner)){
            		//判断是否推送
            		if(judgePublish()){
            			publish();
            		}else{
            			//某个线程重复取(即该频道重复请求)，或者A/B频道都不存在，不做发送处理
            		}
            	}else{//首次进入，如果上次运行的频道和本次来执行的频道不同
            		publish();
            	}
            	
				//得到图时候，休眠200毫秒
				ThreadUtil.sleep(200);
            }
            runningPublishers.remove(channel);
        }
        
        /**
         * 本频道和上次推送频道不同情况下是否推送判断
         * 
         * 现在系统最多会有三个频道：A、B、nuclear
         * 推送条件：频道中不能同时存在A和B，但是这两者必须存在一个
         * 
         * @return true 推送；false 不推送
         */
        private boolean judgePublish() {
        	//A和B频道都存在，频道重复请求
        	if(runningPublishers.containsKey(Constant.CHANNEL_A) && runningPublishers.containsKey(Constant.CHANNEL_B)) {
        		return false;
        	}
        	//存在频道A或B
        	if(runningPublishers.containsKey(Constant.CHANNEL_A) || runningPublishers.containsKey(Constant.CHANNEL_B)) {
				return true;
			}
        	//其余情况都不推送
        	return false;
        }
        
        /**
         * 广播到页面
         */
        private void publish(){
//        	if(getRunningPackageModel() != null) {
//				Gson gson = new Gson();
//				String json = gson.toJson(getRunningPackageModel());
//				if(json == null || json.isEmpty()) return ;
//				json = JsonUtil.transformJson("image",json);
//				broadcaster.broadcast(json);
//            	//获取完毕资源后释放锁，让另一个线程可以获取资源
//				setLastRunner(channel);
//				setRunningPackageModel(null);
//			} 
        }
        
        @Override
        public void onEmpty() {
            shouldRun = false;
            LOG.debug("Shutting down multicast thread for channel {}, no subscribers connected.", channel);
        }

        @Override
        public void onIdle() {
            //nothing needed
        }

        @Override
        public void onDestroy() {
            shouldRun = false;
        }
    }
    
//    /**
//	 * @return the runningPackageModel
//	 */
//	public synchronized RunningPackageModel getRunningPackageModel() {
//		return runningPackageModel;
//	}
//
//	/**
//	 * @param runningPackageModel the runningPackageModel to set
//	 */
//	public synchronized void setRunningPackageModel(RunningPackageModel runningPackageModel) {
//		this.runningPackageModel = runningPackageModel;
//	}

	public synchronized String getLastRunner() {
		return lastRunner;
	}

	public synchronized void setLastRunner(String runner) {
		lastRunner = runner;
	}
}
