package com.example.demo;

import com.inspur.zdp.sdk.ZDPConnection;
import com.inspur.zdp.sdk.ZDPConnectionInfo;
import com.inspur.zdp.sdk.ZDPPortal;
import com.inspur.zdp.sdk.ZDPPubSub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangpl
 * @date 2022/5/16
 */
@Configuration
public class ZDPConfig {

    /**
     * ZDP ip
     */
    @Value("${zdpip}")
    public String ip;

    /**
     * ZDP port
     */
    @Value("${zdpport}")
    public String port;

    /**
     * ZDP key file path
     */
//    @Value("${zdp.key.path}")
//    public String keyPath;

    @Bean("zdpConnection")
    public ZDPConnectionInfo getConnectionInfo() {
        ZDPConnectionInfo zdpConnection = new ZDPConnectionInfo();
        zdpConnection.setIp(ip);
        zdpConnection.setPort(port);
        //zdpConnection.setX509FilePath(keyPath);
        return zdpConnection;
    }

    @Bean("zdpPortal")
    public ZDPPortal getPortal() {
        ZDPConnection connect = ZDPConnection.connect(getConnectionInfo());
        return new ZDPPortal(connect);
    }

    @Bean("zdpPubSub")
    public ZDPPubSub getPubSub() {
        return getPortal().getZDPPubSub();
    }
}
