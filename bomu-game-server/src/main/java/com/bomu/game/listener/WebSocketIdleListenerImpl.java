package com.bomu.game.listener;

import com.bomu.game.socket.protobuf.listener.WebSocketIdleListener;
import com.bomu.game.socket.protobuf.webSocket.WebSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * socket 空闲监听
 */
@Component
public class WebSocketIdleListenerImpl implements WebSocketIdleListener {
    private static final Logger logger = LogManager.getLogger(WebSocketIdleListenerImpl.class);

    @Override
    public void idle(WebSocket webSocket) {
        logger.info("webSocket连接空闲: " + webSocket.toString());
//        webSocket.close();
    }
}
