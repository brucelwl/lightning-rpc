package com.bruce.lightning.rpc.server.mapping;

import com.bruce.lightning.rpc.common.RpcMethodUtil;
import com.bruce.lightning.rpc.common.RpcRequest;
import com.bruce.lightning.rpc.common.RpcResponse;
import com.bruce.lightning.rpc.server.annotation.ExcludeRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class BeanMethodContext {
    private static final Logger logger = LoggerFactory.getLogger(BeanMethodContext.class);

    private static final ConcurrentHashMap<String, BeanMethod> rpcBeanMap = new ConcurrentHashMap<>();


    public static void addServiceBean(Object serviceBean) {
        Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new IllegalStateException(serviceBean.getClass().getName() + " is not an interface implementation class");
        }

        ExcludeRpc excludeRpcInterface = serviceBean.getClass().getAnnotation(ExcludeRpc.class);

        for (Class<?> rpcInterface : interfaces) {
            if (excludeRpcInterface != null && Arrays.asList(excludeRpcInterface.exclude()).contains(rpcInterface)) {
                continue;
            }
            Method[] declaredMethods = rpcInterface.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                //只有public方法才能发布为远程服务
                int modifiers = declaredMethod.getModifiers();
                if ((modifiers == (Modifier.PUBLIC | Modifier.ABSTRACT)) || modifiers == Modifier.PUBLIC) {
                    String fullMethodName = RpcMethodUtil.getFullMethodName(declaredMethod);
                    BeanMethod beanMethod = new BeanMethod(serviceBean, declaredMethod);
                    rpcBeanMap.put(fullMethodName, beanMethod);
                    logger.info("rpc service:" + fullMethodName);
                }
            }
        }
    }

    public static ConcurrentHashMap<String, BeanMethod> getRpcBeanMap() {
        return rpcBeanMap;
    }

    //中介者模式
    public static RpcResponse process(RpcRequest request) {
        String fullMethodName = request.getFullMethodName();
        BeanMethod beanMethod = rpcBeanMap.get(fullMethodName);

        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getId());

        if (beanMethod == null) {
            logger.warn("未找到服务方法{}", fullMethodName);
            response.setCode("not found");
            response.setErrorMesg("未找到服务方法:" + fullMethodName);
            return response;
        }
        Object bean = beanMethod.getBean();
        Method method = beanMethod.getMethod();
        Object[] args = request.getArgs();
        try {
            Object result = method.invoke(bean, args);
            response.setResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrorMesg(e.getMessage());
        }
        return response;
    }

}
