package com.bruce.lightning.rpc.server;

import com.bruce.lightning.rpc.server.initial.marshalling.MarshallingServerHandlerChannelInitializer;
import com.bruce.lightning.rpc.util.PlatformUtil;
import com.bruce.lightning.rpc.util.ReflectUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *  ChannelOption.SO_BACKLOG:指定了内核为此套接口排队的最大连接数,对于给定的监听套接口
 *  内核维护两个队列:未连接队列和已连接队列,三次握手完成后,将会从未完成队列移动到已完成
 *  队列的尾部,当进程调用accept时,从已完成队列的头部取出一个给进程.
 *  ChannelOption.SO_BACKLOG,被规定为两个队列总和的最大值,大多数实现
 *  默认值为5,在高并发的情况下明显不够,netty,默认设置为windows200,其他为128
 * </pre>
 */
public class LightningServer {
    private static final Logger log = LoggerFactory.getLogger(LightningServer.class);

    private EventLoopGroup acceptGroup;
    private EventLoopGroup workerGroup;

    private int port = 8088;

    public LightningServer(int port) {
        this.port = port;
    }

    /**
     * 同步连接
     */
    public void startSync() {
        DefaultThreadFactory acceptThreadFactory = new DefaultThreadFactory("RpcServerAccept-");
        ReflectUtils.set(acceptThreadFactory, DefaultThreadFactory.class, "prefix", "RpcServerAccept-");

        DefaultThreadFactory workerThreadFactory = new DefaultThreadFactory("RpcServerWorker-");
        ReflectUtils.set(workerThreadFactory, DefaultThreadFactory.class, "prefix", "RpcServerWorker-");

        acceptGroup = PlatformUtil.isLinux() ? new EpollEventLoopGroup(1, acceptThreadFactory) : new NioEventLoopGroup(1, acceptThreadFactory);
        workerGroup = PlatformUtil.isLinux() ? new EpollEventLoopGroup(workerThreadFactory) : new NioEventLoopGroup(workerThreadFactory);
        Class<? extends ServerSocketChannel> serverSocketChannelClass = PlatformUtil.isLinux() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptGroup, workerGroup)
                .channel(serverSocketChannelClass)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, false) //默认为false
                .childHandler(new MarshallingServerHandlerChannelInitializer());

        try {
            //异步建立连接
            bootstrap.bind(port).sync();

            //netty启动后创建zk临时节点,创建之前先检测是否存在节点,存在先删除再创建
            // if (existZkPath()) {
            //     deleteZkPath();
            // }
            // createZkPath();

            log.info("Lightning rpc server started !!! port:{}", port);

        } catch (Exception e) {
            log.error("Lightning rpc server 动异常:", e);
            this.close();
        }


    }

    // private boolean existZkPath() throws Exception {
    //     InetAddress localHost = InetAddress.getLocalHost();
    //     Stat stat = ZKCuratorFactory.create().checkExists()
    //             .forPath(ZKConstants.SERVER_PATH + "/" + localHost.getHostAddress() + ":" + port);
    //     return stat != null;
    // }
    //
    // private void createZkPath() throws Exception {
    //     InetAddress localHost = InetAddress.getLocalHost();
    //     ZKCuratorFactory.create().create()
    //             .creatingParentsIfNeeded()
    //             .withMode(CreateMode.EPHEMERAL) //创建ZK临时节点,连接断开后删除
    //             .forPath(ZKConstants.SERVER_PATH + "/" + localHost.getHostAddress() + ":" + port);
    //     log.info("netty 服务端注册到Zookeeper服务器");
    // }
    //
    // private void deleteZkPath() throws Exception {
    //     InetAddress localHost = InetAddress.getLocalHost();
    //     ZKCuratorFactory.create().delete()
    //             .guaranteed()
    //             .deletingChildrenIfNeeded()
    //             .forPath(ZKConstants.SERVER_PATH + "/" + localHost.getHostAddress() + ":" + port);
    //     log.info("删除注册的节点");
    // }

    public void close() {
        try {
            // deleteZkPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (acceptGroup != null) {
            acceptGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("destroy Lightning rpc server");
    }


}
