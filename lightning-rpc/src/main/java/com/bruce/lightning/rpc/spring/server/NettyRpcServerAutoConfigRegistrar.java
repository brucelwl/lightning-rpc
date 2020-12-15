package com.bruce.lightning.rpc.spring.server;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class NettyRpcServerAutoConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableNettyRpcServer.class.getName()));
        int port = (int) attributes.get("port");

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NettyRpcServerAutoConfig.class);
        builder.addConstructorArgValue(port);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
}
