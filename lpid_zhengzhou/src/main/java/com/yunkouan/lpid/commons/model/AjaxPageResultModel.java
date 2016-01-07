package com.yunkouan.lpid.commons.model;

import java.util.List;

/**
 * 
 * <P>Title: csc_sh</P>
 * <P>Description: Ajax分页返回对象</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月5日-下午2:51:13</P>
 * @author  yangli
 * @version 1.0.0
 */
public class AjaxPageResultModel<T> {

	private int pageSize;// 页面显示条数
	private int totalItems;// 数据库总记录数
	private List<T> list;// 当前显示记录
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
	
}
