package com.bomu.socket.client;

import com.bomu.socket.protocol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.MetaDataBase;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class WebSocketClient {
    protected final static Logger logger = LogManager.getLogger(WebSocketClient.class);

    public static String sendAndClose(String wsUrl, Protocol data) throws Exception {
        MetaDataBase.MetaData message = null;

        URI uri = new URI(wsUrl);
        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        final int port;
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme) || "http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }
        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            throw new RuntimeException("Only WS(S) is supported.");
        }
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            WebSocketClientHandler handler = new WebSocketClientHandler();

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            // 解码（将字节流转化成程序中使用的数据类型）
                            p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(2 * 1024 * 1024, 0, 4, 0, 4));
                            p.addLast("protobufDecoder", new ProtobufDecoder(MetaDataBase.MetaData.getDefaultInstance()));

                            // 编码（将程序中的数据类型转换成字节流）
                            p.addLast("frameEncoder", new LengthFieldPrepender(4));
                            //ProtobufEncoder将pb消息替代netty自己的ByteBuf
                            p.addLast("protobufEncoder", new ProtobufEncoder());

                            p.addLast("handler", handler);
                        }
                    })
            ;
            //发起同步连接
            ChannelFuture channelFuture = bootstrap.connect(host, port);
            Channel ch = channelFuture.channel();
            ch.closeFuture().addListener(cf -> {
                ch.closeFuture().sync();
            });
            ChannelPromise promise = handler.sendMessage(data.getMetaData());
            promise.await(100, TimeUnit.MILLISECONDS);
            message = handler.getResponse();
        } finally {
            group.shutdownGracefully();
        }
        if (message != null) {
            return message.getData();
        }
        return null;
    }

}