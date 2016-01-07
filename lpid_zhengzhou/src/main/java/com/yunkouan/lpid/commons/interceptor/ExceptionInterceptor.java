package com.yunkouan.lpid.commons.interceptor;

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
 * <P>Description: 异常拦截器</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月17日-下午7:10:59</P>
 * @author yangli
 * @version 1.0.0
 */
public class ExceptionInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);
    
    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler) throws Exception {  
        String requestUri = request.getRequestURI();
        
        // 判断用户是否登录
        HttpSession session = request.getSession();
        if (session.getAttribute(ParamsUtil.USER_SESSION_KEY) == null  || requestUri.contains("/resources/")) {
           // TODO throw new AuthorizationException();
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
        logger.debug("postHandlepostHandle:============================"); 
    }  
      
    /**
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex)  
            throws Exception {  
        logger.debug("afterCompletionafterCompletion:============================"); 
    }  


}
