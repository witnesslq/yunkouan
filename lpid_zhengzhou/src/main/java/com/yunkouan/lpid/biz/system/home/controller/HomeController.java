package com.yunkouan.lpid.biz.system.home.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunkou.common.util.BeanMapper;
import com.yunkou.common.util.ParamsUtil;
import com.yunkouan.lpid.biz.generic.controller.GenericController;
import com.yunkouan.lpid.biz.system.home.service.HomeService;
import com.yunkouan.lpid.biz.system.operator.bo.OperatorModel;
import com.yunkouan.lpid.commons.model.ResultData;
import com.yunkouan.lpid.persistence.entity.Menu;
import com.yunkouan.lpid.persistence.entity.Operator;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 首页面</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月1日-上午11:27:21</P>
 * @author yangli
 * @version 1.0.0
 */
@Controller
@RequestMapping("/home")
public class HomeController extends GenericController{
    private final static String filePath = "system/home";
    
    @Autowired
    private HomeService homeService;
    
    /**
     * 登录初始化页面
     * 
     * @param model
     * @return 初始页面地址
     * @since 1.0.0
     * 2014年9月19日-上午11:10:17
     */
    @RequestMapping(value = "/initpage")
    public String initPage(HttpServletRequest req, ModelMap model) {
    	 Operator operator = (Operator)req.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
         List<Menu> menuList = homeService.findMenuList(operator);
         if(CollectionUtils.isEmpty(menuList)) {
             errorMessage = "该用户权限没有分配，请重新分配";
             return filePath + "/initpage";
         }
         
         model.put("menuList", menuList);
         
         return filePath + "/initpage";
    }
    
    /**
     * 更改密码
     * 
     * @param operator 操作员 
     * @param currentPassword 原来的密码
     * @param newPassword 修改的密码
     * @return 1：成功  2：原密码输入有误 3: 更新时发生错误
     * @since 1.0.0
     * 2014年10月16日-下午6:46:47
     */
    @RequestMapping(value="/updatePassword")
    public void updateOperatePassword(HttpServletRequest req, HttpServletResponse res,
            @RequestParam String currentPassword, @RequestParam String newPassword, ModelMap model) {
        Operator operator = (Operator)req.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
        int result = homeService.updateOperatePassword(operator, currentPassword, newPassword);
        PrintWriter out;
        try {
            out = res.getWriter();
            out.print(result);
        } catch (Exception e) {
            logger.error("赋值给操作员，初始角色时发生错误", e);
        }
        
    }
    
}
