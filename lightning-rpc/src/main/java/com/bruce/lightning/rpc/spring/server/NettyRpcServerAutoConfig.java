package com.bruce.lightning.rpc.spring.server;

import com.bruce.lightning.rpc.server.LightningServer;
import com.bruce.lightning.rpc.server.mapping.BeanMethodContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.annotation.Order;


@Order(200)
public class NettyRpcServerAutoConfig implements BeanPostProcessor, SmartLifecycle, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServerAutoConfig.class);

    private LightningServer lightningServer;

    private int port;

    public NettyRpcServerAutoConfig(int port) {
        this.port = port;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            BeanMethodContext.addServiceBean(bean);
        }
        return bean;
    }

    @Override
    public void destroy() throws Exception {
        if (lightningServer != null) {
            lightningServer.close();
        }
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE - 50000;
    }

    private boolean isRunning = false;

    @Override
    public void start() {
        logger.info("SmartLifecycle start");
        isRunning = true;
        try {
            lightningServer = new LightningServer(port);
            lightningServer.startSync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        logger.info("SmartLifecycle stop");
    }

    /**
     * <pre>
     *     返回false时执行, org.springframework.context.Lifecycle#start()
     *     返回true时执行, org.springframework.context.Lifecycle#stop()
     * </pre>
     */
    @Override
    public boolean isRunning() {
        logger.info("SmartLifecycle isRunning:{}", isRunning);
        return isRunning;
    }
}
