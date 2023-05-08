package com.inspur.zdp.agent;

import com.google.gson.Gson;
import com.inspur.zdp.agent.grpcs.HeaderClientInterceptor;
import com.inspur.zdp.sdk.ZDPConnectionInfo;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sunxin
 * @date 12.23 16:30
 */
public class AgentContext {

    private static Logger logger =  LoggerFactory.getLogger(AgentContext.class);

    private Map<String, Object> beanMap = new HashMap<String, Object>();

    private static boolean mockMode = false;

    /** 重试策略 */
    protected Map<String, ?> getRetryingServiceConfig() {
        /**
         * "retryPolicy": {
         *   "maxAttempts": 4,
         *   "initialBackoff": "1s",
         *   "maxBackoff": "1s",
         *   "backoffMultiplier": 2,
         *   "retryableStatusCodes": [
         *     "UNAVAILABLE"
         *   ]
         * }
         */
        String config = "\"retryPolicy\": {\n" +
                "  \"maxAttempts\": 4,\n" +
                "  \"initialBackoff\": \"1s\",\n" +
                "  \"maxBackoff\": \"1s\",\n" +
                "  \"backoffMultiplier\": 2,\n" +
                "  \"retryableStatusCodes\": [\n" +
                "    \"UNAVAILABLE\"\n" +
                "  ]\n" +
                "}";
        return new Gson().fromJson("", Map.class);
    }

    private AgentContext(ZDPConnectionInfo zdpConnectionInfo){

        //1.初始化channel
        NettyChannelBuilder nettyChannelBuilder = NettyChannelBuilder.forAddress(zdpConnectionInfo.getIp(), Integer.valueOf(zdpConnectionInfo.getPort()))
                .defaultServiceConfig(getRetryingServiceConfig())
                .enableRetry()
                .usePlaintext();

        //1.1.增加请求头拦截器
        Map<String, String> headerMap = new HashMap<String, String>(1);
        //添加大小写配置请求头
        headerMap.put(ZDPConnectionInfo.LOWERCASE_HEADER, String.valueOf(zdpConnectionInfo.getLowercaseInt()));
        ClientInterceptor interceptor = new HeaderClientInterceptor(headerMap);
        nettyChannelBuilder.intercept(interceptor);

        //1.2.根据配置判断是否启用TLS层
        if (zdpConnectionInfo.useTLS()){
            SslContext sslContext = null;
            try {
                sslContext = GrpcSslContexts
                        .forClient()
                        .trustManager(new File(zdpConnectionInfo.getX509FilePath()))
                        .build();
                nettyChannelBuilder
                        //同：useTransportSecurity()
                        .negotiationType(NegotiationType.TLS)
                        .sslContext(sslContext);
            } catch (SSLException e) {
                logger.error("连接阶段出现问题，TLS安全协议层上下文初始化失败，请联系开发者");
            }
        }
        //1.3.初始化channel
        ManagedChannel channel = nettyChannelBuilder.build();

        //2.默认的GRPC连接
        AgentServer agentServer = AgentServer.connect(zdpConnectionInfo, channel);
        DeviceServer deviceServer = DeviceServer.connect(zdpConnectionInfo, channel);
        EndpointServer endpointServer = EndpointServer.connect(zdpConnectionInfo, channel);
        MessageQueueServer mqServer = MessageQueueServer.connect(zdpConnectionInfo, channel);
        DbServer dbServer = DbServer.connect(zdpConnectionInfo, channel);
        RuleServer ruleServer = RuleServer.connect(zdpConnectionInfo, channel);
        VirtualEndpointServer virtualEndpointServer = VirtualEndpointServer.connect(zdpConnectionInfo, channel);
        DeviceGroupServer deviceGroupServer = DeviceGroupServer.connect(zdpConnectionInfo, channel);
        TsServer tsServer = TsServer.connect(zdpConnectionInfo, channel);

        beanMap.put(AgentServer.class.getName(), agentServer);
        beanMap.put(DeviceServer.class.getName(), deviceServer);
        beanMap.put(EndpointServer.class.getName(), endpointServer);
        beanMap.put(MessageQueueServer.class.getName(), mqServer);
        beanMap.put(DbServer.class.getName(), dbServer);
        beanMap.put(RuleServer.class.getName(), ruleServer);
        beanMap.put(VirtualEndpointServer.class.getName(), virtualEndpointServer);
        beanMap.put(DeviceGroupServer.class.getName(), deviceGroupServer);
        beanMap.put(TsServer.class.getName(), tsServer);
    }

    private AgentContext(){}

    private static volatile AgentContext agentContext = null;

    private static final Object lock = new Object();

    public static AgentContext InitContext(ZDPConnectionInfo zdpConnectionInfo) {
        if (agentContext == null) {
            synchronized (lock) {
                if (agentContext == null) {
                    agentContext = new AgentContext(zdpConnectionInfo);
                }
            }
        }
        return agentContext;
    }
    public static AgentContext getContext() {
        if (agentContext == null) {
            logger.error("请先初始化上下文(InitContext(ZDPConnectionInfo zdpConnectionInfo))");
        }
        return agentContext;
    }

    public static AgentContext runWithMockMode(){
        if (agentContext == null) {
            synchronized (lock) {
                if (agentContext == null) {
                    agentContext = new AgentContext();
                }
            }
        }
        mockMode = true;
        logger.info("running in mock mode ...");
        return agentContext;
    }


    /**
     * 根据类型名称获取所需类型的对象，类型需正确指定避免转换报错
     * @param name 名称
     * @param clazz 类型
     * @param <T>
     * @return
     */
    public <T> T getBean(String name, Class<T> clazz) {
        return (T) this.beanMap.get(name);
    }

    /**
     * 根据类型名称获取所需类型的对象，类型需正确指定避免转换报错
     * @param clazz 类型
     * @param <T>
     * @return
     */
    public <T> T getBean(Class<T> clazz) {
        return (T) this.beanMap.get(clazz.getName());
    }

    public void putBean(String name, Object object) {
        this.beanMap.put(name, object);
    }

    public static boolean isMockMode () {
        return mockMode;
    }
}
