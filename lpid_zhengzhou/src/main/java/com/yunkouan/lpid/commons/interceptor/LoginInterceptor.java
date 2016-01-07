package com.yunkouan.lpid.commons.interceptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yunkou.common.util.ParamsUtil;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 登录拦截器</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月17日-下午7:10:59</P>
 * @author yangli
 * @version 1.0.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    //不用拦截的URL
    private Set<String> excludedUrls = new HashSet<String>();
    
    public void setExcludedUrls(Set<String> excludedUrls) {
        this.excludedUrls = excludedUrls;
    }
    
    //TODO 不用拦截的URL要做在配置文件中，暂时用在这里.如果后面是变化的则在最后的
    {
        excludedUrls.add("/login");
        excludedUrls.add("/ediReceipt");
        excludedUrls.add("/websockets");
        excludedUrls.add("/uploadFiles");
        excludedUrls.add("/sorting");
        excludedUrls.add("/isSorting");
        excludedUrls.add("/testQuery");
        excludedUrls.add("/push");
        excludedUrls.add("/plcHandle");
    }
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        //过滤不被拦截的URL
        for (String url : excludedUrls) {
            //通过URL的结尾进行判断     和上位机交互的链接全部不拦截("wincc")
        	if (requestUri.endsWith(url) || requestUri.contains("/resources/")) {
                return true;
            }
        }
        
        // 判断用户是否登录
        HttpSession session = request.getSession();
        if (session.getAttribute(ParamsUtil.USER_SESSION_KEY) == null) {
            // TODO throw new AuthorizationException();
            //导航到登录页面
            response.sendRedirect(request.getServletContext().getContextPath() + "/index.jsp");
            
        } else {
            return true;
        }
        
        logger.debug("请求地址:" + requestUri + "被拦截"); 
        return false;  
    }  
      
    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler,  
            ModelAndView modelAndView) throws Exception {  
    }  
      
    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex)  
            throws Exception {  
    }  

}
