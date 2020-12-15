package com.bruce.lightning.rpc.spring.client;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by bruce on 2019/1/11 21:32
 */
public class NettyRpcClientAutoConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableNettyRpcClient.class.getName()));

        String host = attributes.getString("host");
        int port = (int) attributes.get("port");

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NettyRpcClientAutoConfig.class);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        builder.addConstructorArgValue(host);
        builder.addConstructorArgValue(port);

        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
}
