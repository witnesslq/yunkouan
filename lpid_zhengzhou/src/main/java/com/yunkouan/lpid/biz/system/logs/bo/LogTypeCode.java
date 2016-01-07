package com.yunkouan.lpid.biz.system.logs.bo;

/**
 * 
 * <P>Title: csc_sh</P>
 * <P>Description: 记录日志用的对应类型</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年10月16日-下午3:28:21</P>
 * @author yangli
 * @version 1.0.0
 */
public enum LogTypeCode {
    
    /* Log类型编码 */
    LOGIN_LOGOUT(1),//登陆管理
    SCREEN_COMPARE(2),//智能查验
    ROLE(3),//角色管理
    OPEREATOR(4);//用户管理 
    
    private final int code;

    private LogTypeCode(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}