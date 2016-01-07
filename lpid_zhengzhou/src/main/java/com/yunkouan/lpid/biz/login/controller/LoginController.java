package com.yunkouan.lpid.biz.login.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkou.common.util.BeanMapper;
import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.ParamsUtil;
import com.yunkouan.lpid.biz.generic.controller.GenericController;
import com.yunkouan.lpid.biz.login.service.LoginService;
import com.yunkouan.lpid.biz.system.logs.bo.LogTypeCode;
import com.yunkouan.lpid.biz.system.logs.service.LogsService;
import com.yunkouan.lpid.biz.system.operator.bo.OperatorModel;
import com.yunkouan.lpid.commons.model.ResultData;
import com.yunkouan.lpid.commons.util.Constant;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 登录处理</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月1日-上午11:27:21</P>
 * @author yangli
 * @version 1.0.0
 */
@Controller
@RequestMapping("/login")
public class LoginController extends GenericController{
	private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private LoginService loginService;
    @Autowired
    private LogsService logsService;
    
    /**
     * 登陆画面错误消息在session中的Key值
     */
    private static String LOGIN_ERROR_MSG = "loginErrorMsg";
    
    /**
     * 跳转到主画面
     * @return
     */
    @RequestMapping(value="initPage")
    public String initPage(){
    	return "redirect:/home/initpage";
    }
    
    /**
     * 用户登录
     * 
     * @param model
     * @return 初始页面地址
     * @throws IOException 
     * @since 1.0.0
     * 2014年9月19日-上午11:10:17
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public ResultData login(HttpServletRequest request, 
    		@RequestParam("loginName") String loginName, 
    		@RequestParam("password") String password,
    		@RequestParam("indexPage") String indexPage,
    		ModelMap model) throws IOException {
    	
    	request.getSession().setAttribute("indexPage", indexPage);
    	
    	ResultData resultData = new ResultData();
    	
    	//清空session中的错误值
    	request.getSession().setAttribute(LOGIN_ERROR_MSG, "");
    	
        //检查用户是否存在
        Operator operator = loginService.findOperator(loginName, DigestUtils.md5Hex(password));
        
        //检查用户是否存在
        if(operator != null){
        	//把用户信息放入Session中以便调用
            request.getSession().setAttribute(ParamsUtil.USER_SESSION_KEY, operator);
          
            List<Menu> authList = loginService.findAuthMenuList(operator);
            //将用户的可访问menu存放到session中
            request.getSession().setAttribute(ParamsUtil.AUTH_MENU_SESSION_KEY, authList);
            
            //记录用户登录时间
            operator.setLastLoginTime(new Date());
            String logDetail="登录名：" + loginName + ";登录时间：" + DateFormatUtils.format(operator.getLastLoginTime(), DateTimeUtil.ISO_DATE_TIME_FORMAT);
            logger.info("[操作员登录]:"+logDetail);
            //写操作日志
            logsService.insertLog(operator, LogTypeCode.LOGIN_LOGOUT, logDetail);
            
            resultData.setMessage("登录成功");
            resultData.setStatus("0");
            resultData.setResult("");
        }else {
//            req.getSession().setAttribute(LOGIN_ERROR_MSG, "登录名或密码错误，请重新输入！");
            resultData.setMessage("登录名或密码错误，请重新输入！");
            resultData.setStatus("-1");
            resultData.setResult("");
        }
        return resultData;
    }
    
    /**
     * 登出系统
     * 
     * @return 
     * @since 1.0.0
     * 2014年10月17日-上午10:53:26
     */
    @RequestMapping(value="/logout")
    public String logout(HttpServletRequest request){
    	Object operatorObj = request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
    	Operator operator = (Operator)(operatorObj == null ? null : operatorObj);
        //用户登出记录日志
        if(operator != null) {
            String logDetail="登录名：" + operator.getLoginName() + ";登出时间：" + DateFormatUtils.format(operator.getLastLoginTime()==null?new Date():operator.getLastLoginTime(), DateTimeUtil.ISO_DATE_TIME_FORMAT);
            logger.info("[操作员登出]:"+logDetail);
            logsService.insertLog(operator, LogTypeCode.LOGIN_LOGOUT, logDetail);
        }
        //从session中移除该用户
        request.getSession().removeAttribute(ParamsUtil.USER_SESSION_KEY);
        String indexPage = (String) request.getSession().getAttribute("indexPage");
        request.getSession().invalidate();
        
        if(StringUtils.isNotEmpty(indexPage)) {
        	String contextPath = request.getServletContext().getContextPath();
        	int port = request.getServerPort(); 
        	String scheme = request.getScheme();
        	String serverName = request.getServerName();
        	return "redirect:" + scheme + "://" + serverName + ":" + port + contextPath + "/" + indexPage + ".jsp"; 
        } else {
        	return "/home/initpage"; 
        }
    }
    
    public String getRemortIP(HttpServletRequest request) {
    	  if (request.getHeader("x-forwarded-for") == null) {
    	   return request.getRemoteAddr();
    	  }
    	  return request.getHeader("x-forwarded-for");
    }
}
