package com.lightning.rpc.example.config;

import com.bruce.lightning.rpc.client.LightningClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

//@Configuration
public class LightningRpcClientBootstrap implements ApplicationListener<ApplicationReadyEvent>, DisposableBean {

    private LightningClient lightningClient;

    public LightningClient getNettyRpcClient() {
        return lightningClient;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        lightningClient = new LightningClient();
        try {
            lightningClient.startSync("127.0.0.1", 8088);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (lightningClient != null) {
            lightningClient.close();
        }
    }

}
