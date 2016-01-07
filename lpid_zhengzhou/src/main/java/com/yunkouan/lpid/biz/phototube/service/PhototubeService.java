package com.yunkouan.lpid.biz.phototube.service;

import com.yunkouan.lpid.modbus.model.PhototubeUseageEnum;

/**
 * <P>Title: lpid_zhengzhou</P>
 * <P>Description: 处理光电管触发的信号</P>
 * <P>Copyright: Copyright(c) 2015</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2015年05月28日-01时06分36秒</P>
 * @author yangli
 * @version 1.0.0
 */
public interface PhototubeService {
    
    /**
     * 根据光电管对应PLC存储的箱包编号变化，做相应的处理
     * 
     * @param phototubeSerialNo PLC变量地址
     * @param phototubeValue PLC变量地址对应的值
     * @since 1.0.0
     */
    public void handle(PhototubeUseageEnum phototubeSerialNo,int phototubeValue);
    
}