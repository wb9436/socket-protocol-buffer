package com.bomu.socket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.MetaDataBase;

import java.util.concurrent.TimeUnit;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    protected final Logger logger = LogManager.getLogger(getClass());

    private ChannelHandlerContext ctx;
    private ChannelPromise promise;
    private MetaDataBase.MetaData response;

    public synchronized ChannelPromise sendMessage(Object message) {
        while (ctx == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error("等待ChannelHandlerContext实例化过程中出错", e);
            }
        }
        promise = ctx.newPromise();
        ctx.writeAndFlush(message);
        return promise;
    }

    public MetaDataBase.MetaData getResponse() {
        return response;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("客户端收到消息：{}", msg.toString());

        MetaDataBase.MetaData message = (MetaDataBase.MetaData) msg;
        if (message != null && message.getData() != null) {
            response = message;
            promise.setSuccess();
        }
    }

    /**
     * 处理异常, 一般将实现异常处理逻辑的Handler放在ChannelPipeline的最后
     * 这样确保所有入站消息都总是被处理，无论它们发生在什么位置，下面只是简单的关闭Channel并打印异常信息
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
