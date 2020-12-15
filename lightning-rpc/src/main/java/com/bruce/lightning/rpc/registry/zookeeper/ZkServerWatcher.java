// package com.bruce.lightning.registry.zookeeper;
//
// import lombok.extern.slf4j.Slf4j;
// import org.apache.curator.framework.CuratorFramework;
// import org.apache.curator.framework.api.CuratorWatcher;
// import org.apache.zookeeper.WatchedEvent;
//
// import java.util.List;
//
// /**
//  * Created by bruce on 2019/1/13 14:11
//  */
// @Slf4j
// public class ZkServerWatcher implements CuratorWatcher {
//     @Override
//     public void process(WatchedEvent event) throws Exception {
//         String path = event.getPath();
//         CuratorFramework curatorFramework = ZKCuratorFactory.create();
//         //继续监听
//         curatorFramework.getChildren().usingWatcher(this).forPath(path);
//
//         List<String> serverPaths = curatorFramework.getChildren().forPath(path);
//
//         for (String serverPath : serverPaths) {
//
//             log.info(serverPath);
//
//         }
//
//     }
// }
