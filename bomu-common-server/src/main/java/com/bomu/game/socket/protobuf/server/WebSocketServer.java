package com.bomu.game.socket.protobuf.server;

import com.bomu.game.socket.config.WSConfig;
import com.bomu.game.socket.protobuf.listener.WebSocketStartListener;
import com.bomu.game.socket.protobuf.webSocket.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class WebSocketServer {
    private static final Logger logger = LogManager.getLogger(WebSocketServer.class);

    @Autowired
    private WebSocketStartListener webSocketStartListener;
    @Autowired
    private WebSocketServerInitializer webSocketServerInitializer;

    public void start() {
        int port = WSConfig.getPort();
        // 创建网络服务器
        EventLoopGroup boss = new NioEventLoopGroup();
        // 创建Worker线程
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            webSocketStartListener.process(); //游戏服务器重启执行操作

            ServerBootstrap serverBoot = new ServerBootstrap();
            serverBoot.group(boss, worker)
                    .localAddress(new InetSocketAddress(port))
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(webSocketServerInitializer);

            // backlog表示主线程池中在套接口排队的最大数量，队列由未连接队列（三次握手未完成的）和已连接队列
            serverBoot.option(ChannelOption.SO_BACKLOG, WSConfig.getBacklog());
            // 表示连接保活，相当于心跳机制，默认为7200s
            serverBoot.childOption(ChannelOption.SO_KEEPALIVE, WSConfig.isKeeplive());

            ChannelFuture channelFuture = serverBoot.bind().sync();
            logger.error("Open your web browser and navigate to http://127.0.0.1:" + port + "/");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
