package com.bomu.game.socket.protobuf.listener;

import com.bomu.game.socket.protobuf.webSocket.WebSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * socket 空闲监听
 */
public interface WebSocketIdleListener {
    Logger logger = LogManager.getLogger(WebSocketIdleListener.class);

    default void readIdle(WebSocket websocket) {
        logger.debug("socket 读取数据空闲：" + websocket.toString());
    }

    default void writerIdle(WebSocket websocket) {
        logger.debug("socket 写数据空闲：" + websocket.toString());
    }

    default void idle(WebSocket websocket) {
        logger.debug("socket 空闲：" + websocket.toString());
    }
}
