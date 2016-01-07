package com.yunkouan.lpid.biz.system.operator.controller;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yunkou.common.util.ParamsUtil;
import com.yunkouan.lpid.biz.generic.controller.GenericController;
import com.yunkouan.lpid.biz.system.logs.bo.LogTypeCode;
import com.yunkouan.lpid.biz.system.logs.service.LogsService;
import com.yunkouan.lpid.biz.system.operator.bo.OperatorModel;
import com.yunkouan.lpid.biz.system.operator.service.OperatorService;
import com.yunkouan.lpid.commons.page.Page;
import com.yunkouan.lpid.persistence.entity.Operator;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 操作员管理页面</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月27日-上午11:27:21</P>
 * @author yangli
 * @version 1.0.0
 */
@Controller
@RequestMapping("/operator")
public class OperatorController extends GenericController{
    private final static String filePath = "system/operator";
    @Autowired
    private LogsService logsService;
    @Autowired
    private OperatorService operatorService;
    
    /**
     * 操作员初始化页面
     * 
     * @param model
     * @return 初始页面地址
     * @since 1.0.0
     * 2014年9月19日-上午11:10:17
     */
    @RequestMapping(value = "/initoperator")
    public String initPage(@ModelAttribute("resultForm") OperatorModel queryModel, 
            Integer currentPage, ModelMap model) {
        int count = operatorService.findAllOperatorCount(queryModel);
        // 根据总记录数、每页记录数、当前页码生成分页对象
        page = new Page<Operator>(count, getPageSize(), currentPage == null? 1 : currentPage);
        List<Operator> operatorList = operatorService.findAllOperator(
                page.getFirstResult(), page.getPageSize(), queryModel);
        
        model.put("operatorList", operatorList);
        //点击查询后，保留住查询的参数
        model.addAttribute("queryModel", queryModel);
        model.addAttribute("page", page);
        
        return filePath + "/userManage";
    }
    
    /**
     * 添加操作员
     * 
     * @return
     */
    @RequestMapping(value = "/addoperator")
    public String addOperator(@ModelAttribute("addEditOperatorForm") OperatorModel operatorModel, ModelMap model, HttpServletRequest request) {
        operatorService.addOperator(operatorModel);
        
        // 添加日志
    	Operator opetrator = (Operator) request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
		String desc = "[添加用户]操作员名字：" + operatorModel.getName() + "；操作员登录名：" + operatorModel.getLoginName();
		LogTypeCode type = LogTypeCode.OPEREATOR;
    	logsService.insertLog(opetrator, type, desc);
        return "redirect:/operator/initoperator";
    }
    
    /**
     * 修改操作员
     * 
     * @param OperatorModel
     * @param model
     * @return 
     * @since 1.0.0
     * 2014年9月26日-下午5:57:46
     */
    @RequestMapping(value = "/updateoperator")
    public String updateOperator(@ModelAttribute("addEditOperatorForm") OperatorModel operatorModel, ModelMap model, HttpServletRequest request) {
        operatorService.updateOperator(operatorModel);
        
        // 添加日志
    	Operator opetrator = (Operator) request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
		String desc = "[修改用户]操作员名字：" + operatorModel.getName() + "；操作员登录名：" + operatorModel.getLoginName();
		LogTypeCode type = LogTypeCode.OPEREATOR;
    	logsService.insertLog(opetrator, type, desc);
        
        return "redirect:/operator/initoperator";
    }
    
    /**
     * 重置密码
     * 
     * @param OperatorModel
     * @param model
     * @return 
     * @since 1.0.0
     * 2014年9月26日-下午5:57:46
     */
    @RequestMapping(value = "/resetPassword")
    public String resetPassword(@ModelAttribute("resetPasswordForm") OperatorModel operatorModel, ModelMap model, HttpServletRequest request) {
        operatorService.updateOperatorPassword(operatorModel);
        
        // 添加日志
    	Operator opetrator = (Operator) request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
		String desc = "[重置密码]操作员名字：" + operatorModel.getName() + "；操作员登录名：" + operatorModel.getLoginName();
		LogTypeCode type = LogTypeCode.OPEREATOR;
    	logsService.insertLog(opetrator, type, desc);
    	
        return "redirect:/operator/initoperator";
    }
    
    /**
     * 操作员的权限数据
     * 
     * @param operatorId
     * @param model
     * @return 
     * @since 1.0.0
     * 2014年9月26日-下午6:01:15
     */
    @RequestMapping(value = "/roletree")
    public void initOperatorRole(HttpServletRequest req, HttpServletResponse res, 
            @RequestParam int operatorId, ModelMap model) {
        String roleJson = operatorService.getOperatorRoleTree(operatorId);
      
        logger.debug(roleJson);
      
        PrintWriter out;
        try {
            out = res.getWriter();
            out.print(roleJson);
        } catch (Exception e) {
            logger.error("赋值给操作员，初始角色时发生错误", e);
        }
    }
    
    /**
     * 修改操作员的角色
     * 
     * @param operatorId
     * @param model
     * @return 
     * @throws UnsupportedEncodingException 
     * @since 1.0.0
     * 2014年9月26日-下午5:57:37
     */
    @RequestMapping(value = "/updateOperatorRole")
    public void updateOperatorRole(HttpServletRequest req, HttpServletResponse res,
            @RequestParam int operatorId, @RequestParam(value="roleArray") String roleArray, ModelMap model)
                    throws UnsupportedEncodingException {
        //TODO menuArray参数先乱码，后正常 ？
        String roleJson = req.getParameter("roleArray");
        roleJson = new String(roleJson.getBytes("ISO-8859-1"), "UTF-8"); 
        //返回给前端的值
        boolean result = operatorService.updateOperatorRole(operatorId, roleJson);;
        
        PrintWriter out;
        try {
            out = res.getWriter();
            out.print(result);
        } catch (Exception e) {
            logger.error("更改操作员角色时发生错误", e);
        }
    }

    /**
     * 删除操作员
     * 
     * @param operatorId
     * @param model
     * @return 
     * @since 1.0.0
     * 2014年9月27日-下午2:33:08
     */
    @RequestMapping(value = "/deleteoperator")
    public String deleteOperator(@RequestParam int operatorId, ModelMap model,HttpServletRequest request) {
        operatorService.deleteOperator(operatorId);
        
        // 添加日志
    	Operator opetrator = (Operator) request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
		String desc = "[删除用户]操作员id：" + operatorId;
		LogTypeCode type = LogTypeCode.OPEREATOR;
    	logsService.insertLog(opetrator, type, desc);
    	
        return "redirect:/operator/initoperator";
    }
    
    /**
     * 检查操作员的用户名是否重复
     * 
     * @param roleName 操作员登录名
     * @return 1：没有查询到登录名 2：该登录名已经存在
     */
    @RequestMapping(value = "/checkLoginName")
    public void checkLoginName(HttpServletRequest request, HttpServletResponse response,
            @RequestParam String loginName) {
    	int count = operatorService.checkLoginName(loginName);
    	
    	int resultValue = 1;
    	if(count > 0) { 
    		resultValue = 2;
    	} 
    	
    	PrintWriter out;
        try {
            out = response.getWriter();
            out.print(resultValue);
        } catch (Exception e) {
            logger.error("检查操作员的用户名是否重复出错！", e);
        }
    }
}
