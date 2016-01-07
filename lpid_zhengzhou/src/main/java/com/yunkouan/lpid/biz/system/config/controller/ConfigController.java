package com.yunkouan.lpid.biz.system.config.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkouan.lpid.biz.system.config.service.ConfigService;
import com.yunkouan.lpid.persistence.entity.ParamConfig;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 参数配置</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月18日-下午4:03:13</P>
 * @author  yangli
 * @version 1.0.0
 */
@Controller
@RequestMapping("/config")
public class ConfigController {
	private final static String filePath = "system/config";
	
	 @Autowired
	 private ConfigService configService;
	 
	 /**
	  * 显示参数配置画面
	  * 
	  * @param req
	  * @param model
	  * @return 
	  * @since 1.0.0
	  * 2014年12月18日-下午4:08:06
	  */
	 @RequestMapping(value = "/paramConfig")
	 public String paramConfig(HttpServletRequest req, ModelMap model) {
        return filePath + "/paramConfig";
	 }
	 
	 /**
	  * 检索所有配置参数
	  * 
	  * @param req
	  * @param model 
	  * @since 1.0.0
	  * 2014年12月18日-下午4:11:22
	  */
	 @RequestMapping(value = "/queryParamConfig")
	 public String queryParamConfig(HttpServletRequest req, ModelMap model) {
		 List<ParamConfig> parameters = configService.findConfigs();
//		 Gson gson = new Gson();
//		 gson.toJson(parameters);
		 model.put("parameters", parameters);
		 return filePath + "/paramConfig";
	 }
	 
	 /**
	  * 更新剩余标签数量
	  */
	 @RequestMapping(value = "/updateLabelRemain")
	 @ResponseBody
	 public void updateLabelRemain(){
		 configService.updateLabelRemain();
	 }
	 
	 /**
	  * 更换标签
	  * @param labelTotal
	  * @return
	  */
	 @RequestMapping(value = "/changeLabel")
	 public String changeLabel(String labelTotal) {
		 List<ParamConfig> parameters = configService.findConfigs();
		 if(parameters.isEmpty()) {
			 parameters = new ArrayList<>();
			 ParamConfig parameterLabelTotal = new ParamConfig();
			 parameterLabelTotal.setName("labelTotal");
			 parameterLabelTotal.setValue(labelTotal);
			 parameters.add(parameterLabelTotal);
			 
			 ParamConfig parameterLabelRemain = new ParamConfig();
			 parameterLabelRemain.setName("labelRemain");
			 parameterLabelRemain.setValue(labelTotal);
			 parameters.add(parameterLabelRemain);
		 } else {
			 for (ParamConfig parameter : parameters) {
				 String name = parameter.getName();
				 if(name.equalsIgnoreCase("labelTotal") || name.equalsIgnoreCase("labelRemain")){
					 parameter.setValue(labelTotal);
				 } 
			 }
		 }
		 configService.updateConfigs(parameters);
		 return "redirect:/config/queryParamConfig";
	 }
	 
	 /**
	  * 更新参数配置
	  * @param suspect
	  * @return
	  */
	 @RequestMapping(value = "/updateConfigs")
	public String updateConfigs(String suspect) {
		List<ParamConfig> parameters = configService.findConfigs();
		if (!parameters.isEmpty()) {
			for (ParamConfig parameter : parameters) {
				if(parameter.getName().equalsIgnoreCase("suspect")) {
					parameter.setValue(suspect);
				}
			}
			configService.updateConfigs(parameters);
		}
		return "redirect:/config/queryParamConfig";
	}
}
