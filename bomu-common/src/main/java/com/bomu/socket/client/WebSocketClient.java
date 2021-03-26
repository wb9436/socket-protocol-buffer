package com.bomu.socket.client;

import com.bomu.socket.protocol.Protocol;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.MetaDataBase;

import java.net.URI;
import java.util.List;

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
        // 创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建启动服务
            Bootstrap bootstrap = new Bootstrap();
            // 注册管理
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            // HTTP请求的解码和编码
                            p.addLast(new HttpClientCodec());
                            // 支持参数对象解析， 比如POST参数， 设置聚合内容的最大长度
                            // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse，
                            // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
                            p.addLast(new HttpObjectAggregator(65536));

                            // 解码器，通过Google Protocol Buffers序列化框架动态的切割接收到的ByteBuf
                            p.addLast(new ProtobufVarint32FrameDecoder());
                            // Google Protocol Buffers 长度属性编码器
                            p.addLast(new ProtobufVarint32LengthFieldPrepender());

                            // 协议包解码
                            p.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
                                @Override
                                protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame,
                                                      List<Object> objs) throws Exception {
                                    if (frame instanceof BinaryWebSocketFrame) {
                                        ByteBuf buf = ((BinaryWebSocketFrame) frame).content();
                                        objs.add(buf);
                                        buf.retain();
                                    }
                                }
                            });
                            // 协议包编码
                            p.addLast(new MessageToMessageEncoder<MessageLiteOrBuilder>() {
                                @Override
                                protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) {
                                    ByteBuf result = null;
                                    if (msg instanceof MessageLite) {
                                        // 没有build的Protobuf消息
                                        result = Unpooled.wrappedBuffer(((MessageLite) msg).toByteArray());
                                    }
                                    if (msg instanceof MessageLite.Builder) {
                                        // 经过build的Protobuf消息
                                        result = Unpooled.wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
                                    }
                                    // 将Protbuf消息包装成BinaryFrame消息
                                    WebSocketFrame frame = new BinaryWebSocketFrame(result);
                                    out.add(frame);
                                }
                            });
                            // Protobuf消息解码器
                            p.addLast(new ProtobufDecoder(MetaDataBase.MetaData.getDefaultInstance()));
                            // 自定义数据消息处理器
                            p.addLast("client_handler", new WebSocketClientHandler());
                        }
                    });
            // 建立连接
            ChannelFuture future;
            // 进行握手
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
            logger.info("-ready to connect :" + handshaker);
            // 获取连接通道
            Channel ch = bootstrap.connect(host, port).sync().channel();

            //获取数据处理器
            WebSocketClientHandler handler = (WebSocketClientHandler) ch.pipeline().get("client_handler");
            handler.setHandshaker(handshaker);
            // 通过它构造握手响应消息返回给客户端，
            // 同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中，用于WebSocket消息的编解码，
            // 添加WebSocketEncoder和WebSocketDecoder之后，服务端就可以自动对WebSocket消息进行编解码了
            handshaker.handshake(ch);
            // 阻塞等待是否握手成功
            future = handler.handshakeFuture().sync();
            logger.info("----channel:" + future.channel());

            // 发送消息
            ch.writeAndFlush(data.getMetaData());
            // 主动关闭连接
            ch.writeAndFlush(new CloseWebSocketFrame());
            ch.closeFuture().await();

            // 获取返回消息
            message = handler.getMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
        if (message != null) {
            return message.getData();
        }
        return null;
    }

}