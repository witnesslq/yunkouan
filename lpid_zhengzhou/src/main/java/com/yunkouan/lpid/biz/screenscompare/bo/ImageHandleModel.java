package com.yunkouan.lpid.biz.screenscompare.bo;

import com.yunkouan.lpid.commons.model.EnumCode;



/**
 * <P>Title: csc_sh</P>
 * <P>Description:图片处理方式 </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月2日-下午6:03:46</P>
 * @author yangli
 * @version 1.0.0
 */
public enum ImageHandleModel implements EnumCode{

    color(10), //彩色
    monochrome(11), //黑白
    highlight(20), //加亮
    shadow(21), //减暗
    highEnergy(30), //高能
    lowEnergy(31), //低能
    organicReject(40), //有机剔除
    inorganicReject(41), //无机剔除
    
    partStrengthen(50), //局增
    exceedStrengthen(51), //超增
    inverse(60), //反色
    
    otherHandle(999999);//其他未知处理方式
    
    private final int number;

    private ImageHandleModel(int number) {
        this.number = number;
    }
    
    /**
     * @see com.yunkouan.cscsh.commons.model.EnumCode#getNumber()
     */
    @Override
    public int getNumber() {
        return number;
    }

}
