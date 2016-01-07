package com.yunkouan.lpid.modbus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;

/**
 * 
 * <P>Title: yka-lpid</P>
 * <P>Description: Modbus服务初始化</P>
 * <P>Copyright: Copyright(c) 2015</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2015年6月8日-下午2:50:13</P>
 * @author yangli
 * @version 1.0.0
 */
public class ModbusService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private ModbusFactory modbusFactory = new ModbusFactory();
    protected ModbusMaster master;
    
    private String host;
    private int port;
    private IpParameters tcpParameters;
    
    protected ModbusService(String host, int port) {
        this.host = host;
        this.port = port;
        this.tcpParameters = new IpParameters();
        this.tcpParameters.setHost(host);
        this.tcpParameters.setPort(port);
    }
    
    /**
     * @brief tcp连接
     * @param tcpParameters
     */
    public void connection(){
        //modbus对tcp封装，并采取两次重连断开后抛出异常
        try {
            master = modbusFactory.createTcpMaster(tcpParameters, true);
            master.init();
        } catch (ModbusInitException e) {
            logger.error("modbus连接异常，进行重连操作");
        }
    }
}
