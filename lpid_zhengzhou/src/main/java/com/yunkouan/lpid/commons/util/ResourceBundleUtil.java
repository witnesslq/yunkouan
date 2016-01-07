package com.yunkouan.lpid.commons.util;

import java.util.ResourceBundle;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 配置文件读取工具类</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年11月25日-下午4:24:05</P>
 * @author yangli
 * @version 1.0.0
 */
public class ResourceBundleUtil {
    //读取system配置文件中的值
	private final static ResourceBundle system = ResourceBundle.getBundle("system");
	//读取modbus配置文件中的值
	private final static ResourceBundle modbus = ResourceBundle.getBundle("modbus");
	 
	public static String getSystemString(String key){
		return system.getString(key);
	}
	
	public static int getSystemInt(String key){
		return Integer.valueOf(system.getString(key));
	}
	 
	public static String getModbusString(String key){
		return modbus.getString(key);
	}
	public static int getModbusInt(String key){
		return Integer.valueOf(modbus.getString(key));
	}
	
	public static long getSystemLong(String key){
	    return Long.valueOf(system.getString(key));
	}
	
	public static String[] getSystemStringArray(String key){
		return system.getString(key).split(",");
	}
	
}
