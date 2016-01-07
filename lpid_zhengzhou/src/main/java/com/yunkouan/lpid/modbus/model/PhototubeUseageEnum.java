package com.yunkouan.lpid.modbus.model;


/**
 * <P>Title: yka-wincc</P>
 * <P>Description: 根据光电管位置编的序列号</P>
 * <P>Copyright: Copyright(c) 2015</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2015年5月28日-下午6:25:23</P>
 * @author yangli
 * @version 1.0.0
 */
public enum PhototubeUseageEnum implements EnumCode{
	/**
	 * <p>上位机下发急停指令<br>
	 * 皮带急停(X光机不停止)<br>
	 * 值为1时,皮带停止;值为0时,皮带不停止.</p>
	 */
	BELT_STOP(10),
	/**
	 * <p>X光机钥匙开关<br>
	 * 值为1时,关机.</p>
	 */
	XRAY_OFF(11),
	/**
	 * <p>上位机下发X光机启动按钮指令<br>
	 * x光机启动<br>
	 * 值为1时x光机会运行，休眠1秒后马上赋值为0</p>
	 */
	XRAY_STARTUP(12),
	/**
	 * <p>上位机下发范指令屏蔽位<br>
	 * 值为1时,行邮的皮带机不受范德兰德控制;<br>
	 * 值为0时,行邮的皮带机受到范德兰德控制.</p>
	 */
	CONTROLLED(15),
	/**
	 * <p>上位机下发皮带机启动指令<br>
	 * 皮带启动按钮<br>
	 * 值为1时,皮带启动;休眠1秒后,给PLC发送0.</p>
	 */
	BELT_STARTUP(16),
	/**
	 * <p>上位机下发分检物品编号(分拣时,写入包裹编号)</p>
	 */
	WRITE_PAGGAGE_NO(40),
	/**
	 * <p>上位机下发立即推包指令</p>
	 */
	BAG_PUSH(41),
    /**
     * <p>PLC上传给上位机光电管1物品编号(包裹编号)</p> 
     */
	READ_PAGGAGE_NO(100),		
	/**
	 * <p>PLC上传给上位机物品长度(包裹长度,单位cm)</p>
	 */
	PAGGAGE_LENGTH(101), 
	/**
	 * <p>物品经过下降沿标志位上传给上位(没有使用)</p>
	 */
	READ_PAGGAGE_NO2(102),
	/**
	 * <p>推杆推出状态上传给上位</p>
	 */
	PUTTERS_PUSH(120),
	/**
	 * <p>推杆返回状态上传给上位</p>
	 */
	PUTTERS_RETURN(121),
	/**
	 * <p>贴标动作状态上传给上位</p>
	 */
	DECALS(122),
	/**
	 * <p>远极限开关状态上传给上位</p>
	 */
	FAR_ONOFF(123),
	/**
	 * <p>近极限开关状态上传给上位</p>
	 */
	NEAR_ONOFF(124),
	/**
	 * <p>PLC上传给上位机节能模式状态</p>
	 */
	ENERGY(127),
	/**
	 * <p>第一段皮带机运行状态上传给上位</p>
	 */
	FIRSTBELT_RUN(128),
	/**
	 * <p>第二段皮带机运行状态上传给上位</p>
	 */
	SECONDBELT_RUN(129),
	/**
	 * <p>X光机运行状态上传给上位</p>
	 */
	XRAY_MACHINE_RUN(130),
	/**
	 * <p>有急停或范德兰德停止指令上传给上位</p>
	 */
	STOP(131),
	/**
	 * <p>贴标机故障、光电管对偏或被遮挡、推杆系统异常状态上传给上位</p>
	 */
	ALL(132),
	/**
	 * <p>推杆长时间没有回到原点状态上传给上位</p>
	 */
	PUTTERS_ORIGIN(140),
	/**
	 * <p>原点开关故障位上传给上位</p>
	 */
	ORIGIN_ONOFF(141),
	/**
	 * <p>推包器故障位上传给上位</p>
	 */
	BAG_PUSHING(142),
	/**
	 * <p>标签数量</p>
	 */
	LABEL_NUMBER(122);
	
	private int number;
	private PhototubeUseageEnum(int number) {
		this.number = number;
	}
	
	@Override
	public int getNumber() {
		return number;
	}
}
