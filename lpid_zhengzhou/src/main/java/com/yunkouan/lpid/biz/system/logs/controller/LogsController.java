package com.yunkouan.lpid.biz.system.logs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunkouan.lpid.biz.generic.controller.GenericController;
import com.yunkouan.lpid.biz.system.logs.bo.LogTypeModel;
import com.yunkouan.lpid.biz.system.logs.bo.LogsModelQuery;
import com.yunkouan.lpid.biz.system.logs.service.LogsService;
import com.yunkouan.lpid.commons.page.Page;
import com.yunkouan.lpid.persistence.entity.Log;
import com.yunkouan.lpid.persistence.entity.LogType;
import com.yunkouan.lpid.persistence.entity.Operator;
/**
 * <P>Title: ppoiq</P>
 * <P>Description: 日志检索画面</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月25日-下午2:38:03</P>
 * @author  yangli
 * @version 1.0.0
 */
@Controller
@RequestMapping("/logs")
public class LogsController extends GenericController {
	private final static String filePath = "system/logs";

	@Autowired
	private LogsService logsService;

	/**
	 * 初始进入页面
	 * 
	 * @param model
	 * @return 
	 * @since 1.0.0
	 * 2014年10月16日-下午3:22:22
	 */
	@RequestMapping(value = "/logspage")
	public String initPage(ModelMap model) {
	    
	    initLogType(model, -1);
	    
		return filePath + "/logspage";
	}
	
	/**
	 * logType下拉框数据准备
	 * 
	 * @param model
	 * @param defaultSelect 
	 * @since 1.0.0
	 * 2014年10月16日-下午3:21:24
	 */
	private void initLogType(ModelMap model, int defaultSelect) {
	    LogTypeModel logType = new LogTypeModel();  
        logType.setLogTypeSelect(defaultSelect);//设置默认选中项
        //下拉框选择内容
        Map<Integer, String> logTypeMap = new HashMap<Integer, String>();  
        logTypeMap.put(-1, "请选择");
        
        //查询log类型
        List<LogType> logTypeList = logsService.findLogType();
        for (LogType type : logTypeList) {
            logTypeMap.put(type.getId(), type.getName());
        }
        
        model.put("logType", logType);  
        model.put("logTypeMap", logTypeMap);
	}
	
	/**
	 * 分页查询后页面
	 * 
	 * @param req
	 * @param res
	 * @param queryModel
	 * @param currentPage
	 * @param model
	 * @return 
	 * @since 1.0.0
	 * 2014年10月16日-下午3:22:34
	 */
	@RequestMapping(value = "/logList")
	public String logs(HttpServletRequest req,
			HttpServletResponse res,
			@ModelAttribute("resultForm") LogsModelQuery queryModel,
			Integer currentPage, ModelMap model){
		int count = logsService.findLogInfoCount(queryModel);
        // 根据总记录数、每页记录数、当前页码生成分页对象
        page = new Page<Operator>(count, getPageSize(), currentPage == null? 1 : currentPage);
        List<Log> logList = logsService.findAllLogInfo(page.getFirstResult(), page.getPageSize(), queryModel);
        
        model.put("logList", logList);
        //点击查询后，保留住查询的参数
        model.addAttribute("queryModel", queryModel);
        model.addAttribute("page", page);
        
        //设置logType的显示
        initLogType(model,queryModel.getLogTypeSelect());
        
        return filePath + "/logspage";
	}
}
