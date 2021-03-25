package com.bomu.game.socket.protobuf.listener;

import com.bomu.game.socket.protobuf.webSocket.WebSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 断开连接
 */
public interface WebSocketCloseListener {
    Logger logger = LogManager.getLogger(WebSocketCloseListener.class);

    default void process(WebSocket webSocket) {
        logger.info("socket 断开连接：" + webSocket.toString());
    }

}
