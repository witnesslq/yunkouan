package com.yunkouan.lpid.biz.system.config.bo;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: 配置参数model</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月18日-下午4:16:23</P>
 * @author  yangli
 * @version 1.0.0
 */
public class Config {
	private String name;
	private String value;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
