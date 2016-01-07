package com.yunkouan.lpid.biz.screenscompare.bo;


/**
 * <P>Title: ppoiq</P>
 * <P>Description: 坐标信息Model类</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月10日-下午2:33:52</P>
 * @author yangli
 * @version 1.0.0
 */
public class CoordModel {
	private int x1;
	private int y1;
	
	private int x2;
	private int y2;
	
	private int x3;
	private int y3;
	
	private int x4;
	private int y4;
	
	public int getX1() {
		return x1;
	}
	public void setX1(int x1) {
		this.x1 = x1;
	}
	public int getY1() {
		return y1;
	}
	public void setY1(int y1) {
		this.y1 = y1;
	}
	public int getX2() {
		return x2;
	}
	public void setX2(int x2) {
		this.x2 = x2;
	}
	public int getY2() {
		return y2;
	}
	public void setY2(int y2) {
		this.y2 = y2;
	}
	public int getX3() {
		return x3;
	}
	public void setX3(int x3) {
		this.x3 = x3;
	}
	public int getY3() {
		return y3;
	}
	public void setY3(int y3) {
		this.y3 = y3;
	}
	public int getX4() {
		return x4;
	}
	public void setX4(int x4) {
		this.x4 = x4;
	}
	public int getY4() {
		return y4;
	}
	public void setY4(int y4) {
		this.y4 = y4;
	}
	@Override
	public String toString() {
		return "CoordModel [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2="
				+ y2 + ", x3=" + x3 + ", y3=" + y3 + ", x4=" + x4 + ", y4="
				+ y4 + "]";
	}
}