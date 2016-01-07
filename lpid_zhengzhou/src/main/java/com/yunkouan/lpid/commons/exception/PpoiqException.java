package com.yunkouan.lpid.commons.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 本系统异常处理类</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月20日-下午4:35:31</P>
 * @author yangli
 * @version 1.0.0
 */
public class PpoiqException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    //抛出异常的编码
    private ExceptionCode errorCode;
    //用于异常信息存放
    private final Map<String,Object> properties = new TreeMap<String,Object>();

    /**
     * 对异常对象进行包装，返回PpoiqException类型的异常
     * 
     * @param exception 一般异常
     * @param errorCode 异常信息编码
     * @return 
     * @since 1.0.0
     * 2014年12月22日-上午11:11:57
     */
    public static PpoiqException wrap(Throwable exception, ExceptionCode errorCode) {
        if (exception instanceof PpoiqException) {
            PpoiqException se = (PpoiqException)exception;
        	if (errorCode != null && errorCode != se.getErrorCode()) {
                return new PpoiqException(exception.getMessage(), exception, errorCode);
			}
			return se;
        } else {
            return new PpoiqException(exception.getMessage(), exception, errorCode);
        }
    }
    
    /**
     * 对异常对象进行包装，返回PpoiqException类型的异常
     * 
     * @param exception 一般异常
     * @return 
     * @since 1.0.0
     * 2014年12月22日-上午11:16:19
     */
    public static PpoiqException wrap(Throwable exception) {
    	return wrap(exception, null);
    }
    
    /**
     * 创建一个新的实例 PpoiqException.
     *
     * @param errorCode 异常信息编码
     */
    public PpoiqException(ExceptionCode errorCode) {
		this.errorCode = errorCode;
	}

    /**
     * 创建一个新的实例 PpoiqException.
     *
     * @param message 异常信息
     * @param errorCode 异常信息编码
     */
	public PpoiqException(String message, ExceptionCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 创建一个新的实例 PpoiqException.
	 *
	 * @param cause 异常包装类
	 * @param errorCode 异常信息编码
	 */
	public PpoiqException(Throwable cause, ExceptionCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	/**
	 * 
	 * 创建一个新的实例 PpoiqException.
	 *
	 * @param message 异常信息
	 * @param cause 异常包装类
	 * @param errorCode 异常信息编码
	 */
	public PpoiqException(String message, Throwable cause, ExceptionCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	/**
	 * 异常信息打印，参数类型为PrintStream，包装后调用printStackTrace(PrintWriter s)方法
	 * 
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
    public void printStackTrace(PrintStream s) {
        synchronized (s) {
            printStackTrace(new PrintWriter(s));
        }
    }

    /**
     * 异常信息打印
     * 
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintWriter s) { 
        synchronized (s) {
            s.println(this);
            s.println("\t-------------------------------");
            if (errorCode != null) {
	        	s.println("\t" + errorCode + ":" + errorCode.getClass().getName()); 
			}
            for (String key : properties.keySet()) {
            	s.println("\t" + key + "=[" + properties.get(key) + "]"); 
            }
            s.println("\t-------------------------------");
            
            //异常堆栈数组
            StackTraceElement[] trace = getStackTrace();
            for (int i = 0; i < trace.length; i++) {
                s.println("\tat " + trace[i]);
            }
            
            //异常原因
            Throwable ourCause = getCause();
            if (ourCause != null) {
                ourCause.printStackTrace(s);
            }
            s.flush();
        }
    }
    
    public ExceptionCode getErrorCode() {
        return errorCode;
    }
    
    public PpoiqException setErrorCode(ExceptionCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T)properties.get(name);
    }
    
    public PpoiqException set(String name, Object value) {
        properties.put(name, value);
        return this;
    }
    
}
