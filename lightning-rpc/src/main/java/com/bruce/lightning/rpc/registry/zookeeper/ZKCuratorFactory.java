// package com.bruce.lightning.registry.zookeeper;
//
// import lombok.extern.slf4j.Slf4j;
// import org.apache.curator.RetryPolicy;
// import org.apache.curator.framework.CuratorFramework;
// import org.apache.curator.framework.CuratorFrameworkFactory;
// import org.apache.curator.retry.ExponentialBackoffRetry;
//
// @Slf4j
// public class ZKCuratorFactory {
//     private static final String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
//     private static CuratorFramework client = null;
//
//     public static CuratorFramework create() {
//         if (client == null) {
//             synchronized (ZKCuratorFactory.class) {
//                 if (client == null) {
//                     /*RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
//                       client = CuratorFrameworkFactory.builder()
//                       .connectString(connectString)
//                       .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
//                       .namespace("netty_rpc").build();
//                      */
//                     RetryPolicy retryPolicy = new ExponentialBackoffRetry(2000, 5);
//                     client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
//                     client.start();
//                 }
//             }
//         }
//         return client;
//     }
//
//
// }
