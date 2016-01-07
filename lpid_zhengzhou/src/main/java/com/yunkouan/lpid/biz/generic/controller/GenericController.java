package com.yunkouan.lpid.biz.generic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunkouan.lpid.commons.page.Page;
import com.yunkouan.lpid.commons.util.ResourceBundleUtil;

/**
 * 
 * <P>Title: csc_sh</P>
 * <P>Description: Controller基类</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年10月16日-下午7:06:35</P>
 * @author yangli
 * @version 1.0.0
 */
public class GenericController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private static int PAGE_SIZE = Integer.valueOf(ResourceBundleUtil.getSystemString("PAGE_SIZE"));
	protected String errorMessage;//错误消息
	
	/**
	 * 分页信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page page;

	@SuppressWarnings("unchecked")
	public Page getPage() {
		return page;
	}

	@SuppressWarnings("unchecked")
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * 获取每页记录数
	 * 
	 * @return 一页记录数
	 */
	protected int getPageSize() {
		//从配置属性中读取
		return PAGE_SIZE;
	}
}
