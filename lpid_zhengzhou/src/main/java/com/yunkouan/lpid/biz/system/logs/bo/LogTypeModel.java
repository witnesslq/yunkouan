package com.yunkouan.lpid.biz.system.logs.bo;

/**
 * 
 * <P>Title: csc_sh</P>
 * <P>Description:查询log时，下拉框使用 </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年10月15日-下午5:32:12</P>
 * @author yangli
 * @version 1.0.0
 */
public class LogTypeModel {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;//名称
	private String desc;//描述
	private int logTypeSelect;//默认选中

	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public int getLogTypeSelect() {
        return logTypeSelect;
    }
    public void setLogTypeSelect(int logTypeSelect) {
        this.logTypeSelect = logTypeSelect;
    }

}
