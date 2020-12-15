package com.bruce.netty.config;

import com.bruce.netty.websocket.WebsocketInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by bruce on 2018/10/15 22:48
 */
@Slf4j
@ConditionalOnProperty(prefix = "netty.server",name = "type",havingValue = "ws")
@Configuration
public class NettyWebsocketServerBootstrap {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private final int port = 8088;

    @PostConstruct
    public void nettyServerStart() {
        log.info("netty websocket server starting");
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("netty websocket server started on port:" + port);
    }

    @PreDestroy
    public void aa() {
        close();
    }

    private void close() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("destroy netty server thread");
    }

    private void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(2);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebsocketInitializer());
            //使用sync()会阻塞当前线程,程序无法向下执行,可以在子线程中执行,或者不使用sync();
            // ChannelFuture channelFuture = bootstrap.bind(8088).sync();
            ChannelFuture channelFuture = bootstrap.bind(port);
            channelFuture.channel().closeFuture();

        } catch (Exception e) {
            log.error("NettyServerBootstrap-->", e);
            close();
        }
    }


}
