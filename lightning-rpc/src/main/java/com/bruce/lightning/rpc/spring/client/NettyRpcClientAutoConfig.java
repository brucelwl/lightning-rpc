package com.bruce.lightning.rpc.spring.client;

import com.bruce.lightning.rpc.client.LightningClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;

/**
 * Created by bruce on 2019/1/11 20:52
 */
@Order(1000)
public class NettyRpcClientAutoConfig implements BeanPostProcessor, ApplicationListener<ApplicationReadyEvent>, InitializingBean, SmartLifecycle, DisposableBean {

    private int port;
    private String host;

    private LightningClient client;

    /** 优先使用注册中心上的服务地址,否则使用默认的 */
    public NettyRpcClientAutoConfig(String host, int port) {
        this.port = port;
        this.host = host;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        client = new LightningClient();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // CuratorFramework curatorFramework = ZKCuratorFactory.create();
        // try {
        //     //客户端监听zk变化
        //     CuratorWatcher zkServerWatcher = new ZkServerWatcher();
        //     curatorFramework.getChildren().usingWatcher(zkServerWatcher).forPath(ZKConstants.SERVER_PATH);
        //     //获取服务地址
        //     List<String> servers = curatorFramework.getChildren().forPath(ZKConstants.SERVER_PATH);
        //     if (!CollectionUtils.isEmpty(servers)) {
        //         serverAddress = servers.stream().distinct().findAny().orElseGet(() -> serverAddress);
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    @Override
    public void destroy() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            //rpc 仅支持接口类型
            if (rpcReference != null && declaredField.getType().isInterface()) {
                Class<?> rpcInterface = declaredField.getType();
                Object jdkProxy = client.createProxy(rpcInterface, rpcReference.timeout());
                try {
                    declaredField.setAccessible(true);
                    declaredField.set(bean, jdkProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE - 4000;
    }

    @Override
    public void start() {
        client.startSync(host, port);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
