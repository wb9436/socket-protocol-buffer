/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.bomu.game.socket.protobuf.webSocket;


import com.bomu.game.socket.protobuf.dispatcher.Dispatcher;
import com.bomu.game.socket.protobuf.listener.WebSocketCloseListener;
import com.bomu.game.socket.protobuf.listener.WebSocketIdleListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.MetaDataBase;

import java.util.Date;


public class WebSocketServerHandler extends SimpleChannelInboundHandler<MetaDataBase.MetaData> {
    private static final Logger logger = LogManager.getLogger(WebSocketServerHandler.class);

    private WebSocket websocket;
    private WebSocketCloseListener closeListener;
    private WebSocketIdleListener idleListener;

    public WebSocketServerHandler() {
    }

    /**
     * 连接建立
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        websocket = new WebSocket(ctx);
        logger.debug("创建连接: " + websocket.getId() + ":" + "handlerAdded");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MetaDataBase.MetaData frame) throws Exception {
        Dispatcher.dispatcher(websocket, frame);
    }

    /**
     * 连接关闭
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        WebSocketManage.remove(websocket);
        if (closeListener != null) {
            closeListener.process(websocket);
        }
        logger.debug("handlerRemoved websocket colse:" + websocket.toString() + ";" + WebSocketManage.getOnlineCount());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            switch (event.state()) {
                case ALL_IDLE:
                    logger.debug(websocket.getId() + ":" + "ALL_IDLE:userEventTriggered=" + event.state() + new Date());
                    idleListener.idle(websocket);
                    break;
                case READER_IDLE:
                    idleListener.readIdle(websocket);
                    break;
                case WRITER_IDLE:
                    idleListener.writerIdle(websocket);
                    break;
            }
        }
    }

    /**
     * 发生异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.debug("exceptionCaught=" + ctx.channel().id().asShortText());
        this.websocket.close();
    }

    public void setCloseListener(WebSocketCloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public void setIdleListener(WebSocketIdleListener idleListener) {
        this.idleListener = idleListener;
    }

}
