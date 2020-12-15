// package test;
//
// import com.bruce.fastrpc.registry.zookeeper.ZKCuratorFactory;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.curator.framework.CuratorFramework;
// import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
// import org.apache.zookeeper.CreateMode;
//
// @Slf4j
// public class ZkTest {
//     public static void main(String[] args) throws Exception {
//
//         CuratorFramework client = ZKCuratorFactory.create();
//         /*System.out.println(client.getState());
//         String s = client.create().forPath("/netty");
//         System.out.println(s);*/
//
//         // 创建节点
//         String nodePath = "/super/bruce";
//         byte[] data = "superme".getBytes();
//
//         //create(client, nodePath, data);
//         delete(client, "/netty");
//     }
//
//     public static String create(CuratorFramework client, String path) throws Exception {
//         return create(client, path, null);
//     }
//
//     public static String create(CuratorFramework client, String path, byte[] data) throws Exception {
//         ACLBackgroundPathAndBytesable<String> create = client.create()
//                 .creatingParentsIfNeeded()
//                 .withMode(CreateMode.PERSISTENT);
//         //.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
//         //.forPath(path, data);
//
//         String s = data == null ? create.forPath(path) : create.forPath(path, data);
//         log.info("创建节点成功{}", s);
//         return s;
//     }
//
//     public static void delete(CuratorFramework client, String path) throws Exception {
//         client.delete()
//                 .guaranteed()
//                 .deletingChildrenIfNeeded()
//                 .forPath(path);
//         log.info("删除节点成功");
//     }
//
//
// }
