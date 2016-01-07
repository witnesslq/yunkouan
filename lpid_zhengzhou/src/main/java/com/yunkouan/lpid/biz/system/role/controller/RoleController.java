package com.yunkouan.lpid.biz.system.role.controller;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
import com.yunkouan.lpid.biz.system.role.bo.RoleModel;
import com.yunkouan.lpid.biz.system.role.service.RoleService;
import com.yunkouan.lpid.persistence.entity.Operator;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 角色管理页面</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月1日-上午11:27:21</P>
 * @author yangli
 * @version 1.0.0
 */
@Controller
@RequestMapping("/role")
public class RoleController extends GenericController{
    private final static String filePath = "system/role";
    
    @Autowired
    private LogsService logsService;
    @Autowired
    private RoleService roleService;
    
    /**
     * 登录初始化页面
     * 
     * @param model
     * @return 初始页面地址
     * @since 1.0.0
     * 2014年9月19日-上午11:10:17
     */
    @RequestMapping(value = "/initrole")
    public String initPage(ModelMap model) {
        
        model.put("roleList", roleService.findRoles());
        
        return filePath + "/role";
    }
    
    /**
     * 添加角色
     * 
     * @return
     */
    @RequestMapping(value = "/addrole")
    public String addRole(@ModelAttribute("addEditRole") RoleModel roleModel, ModelMap model, HttpServletRequest request) {
    	roleModel.setStatus(1);
    	roleService.addRole(roleModel);
    	
    	// 添加日志
    	Operator opetrator = (Operator) request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
		String desc = "[添加角色]角色类型：" + roleModel.getRoleName() + "；角色描述：" + roleModel.getRoleDescribe();
		LogTypeCode type = LogTypeCode.ROLE;
    	logsService.insertLog(opetrator, type, desc);
        return "redirect:/role/initrole";
    }
    
    /**
     * 修改角色
     * 
     * @param roleModel
     * @param model
     * @return 
     * @since 1.0.0
     * 2014年9月26日-下午5:57:46
     */
    @RequestMapping(value = "/updaterole")
    public String updateRole(@ModelAttribute("addEditRole") RoleModel roleModel, ModelMap model,HttpServletRequest request) {
        roleService.updateRole(roleModel);
        // 添加日志
    	Operator opetrator = (Operator) request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
		String desc = "[修改角色] id：" + roleModel.getRoleId() + "角色类型：" + roleModel.getRoleName() + "；角色描述：" + roleModel.getRoleDescribe();
		LogTypeCode type = LogTypeCode.ROLE;
    	logsService.insertLog(opetrator, type, desc);
        return "redirect:/role/initrole";
    }
    
    /**
     * 角色的权限数据
     * 
     * @param roleId
     * @param model
     * @return 
     * @since 1.0.0
     * 2014年9月26日-下午6:01:15
     */
    @RequestMapping(value = "/menutree")
    public void initRoleAuth(HttpServletRequest req, HttpServletResponse res, 
            @RequestParam int roleId, ModelMap model) {
      String roleMenuJson = roleService.getMenuTree(roleId);
      
      logger.debug(roleMenuJson);
      
      PrintWriter out;
      try {
          out = res.getWriter();
          out.print(roleMenuJson);
      } catch (Exception e) {
          logger.error("获取角色的权限时发生错误", e);
      }
    }
    
    /**
     * 修改角色的权限
     * 
     * @param roleId
     * @param model
     * @return 
     * @throws UnsupportedEncodingException 
     * @since 1.0.0
     * 2014年9月26日-下午5:57:37
     */
    @RequestMapping(value = "/updateRoleAuth")
    public void updateRoleAuth(HttpServletRequest req, HttpServletResponse res,
            @RequestParam int roleId, @RequestParam(value="menuArray") String menuArray, ModelMap model) throws UnsupportedEncodingException {
        //TODO menuArray参数先乱码，后正常 ？
        String menuJson = req.getParameter("menuArray");
        menuJson = new String(menuJson.getBytes("ISO-8859-1"), "UTF-8"); 
        //返回给前端的值
        boolean result = roleService.updateAuth(roleId, menuJson);;
        
        PrintWriter out;
        try {
            out = res.getWriter();
            out.print(result);
        } catch (Exception e) {
            logger.error("修改角色权限时发生错误", e);
        }
    }

    /**
     * 删除角色
     * 
     * @param roleId
     * @param model
     * @return 
     * @since 1.0.0
     * 2014年9月27日-下午2:33:08
     */
    @RequestMapping(value = "/deleterole")
    public String deleteRole(@RequestParam int roleId, ModelMap model,HttpServletRequest request) {
        roleService.deleteRole(roleId);
        
        // 添加日志
    	Operator opetrator = (Operator) request.getSession().getAttribute(ParamsUtil.USER_SESSION_KEY);
		String desc = "[删除角色] 角色id：" + roleId;
		LogTypeCode type = LogTypeCode.ROLE;
    	logsService.insertLog(opetrator, type, desc);
    	
        return "redirect:/role/initrole";
    }
    
    /**
     * 检查角色名称是否重复
     * 
     * @param roleName 角色名称
     * @return 1：没有查询到角色 2：该角色已经存在
     */
    @RequestMapping(value = "/checkRoleName")
    public void checkRoleName(HttpServletRequest request, HttpServletResponse response, 
            @RequestParam String roleName) {
    	int count = roleService.checkRoleName(roleName);
    	
    	int resultValue = 1;
    	if(count > 0) { 
    		resultValue = 2;
    	} 
    	
    	PrintWriter out;
        try {
            out = response.getWriter();
            out.print(resultValue);
        } catch (Exception e) {
            logger.error("检查角色名称是否重复出错！", e);
        }
    }
}
