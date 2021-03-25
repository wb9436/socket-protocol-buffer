package com.bomu.game.listener;

import com.bomu.game.socket.protobuf.listener.WebSocketCloseListener;
import com.bomu.game.socket.protobuf.webSocket.WebSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 断开连接
 */
@Component
public class WebSocketCloseListenerImpl implements WebSocketCloseListener {
    private static final Logger logger = LogManager.getLogger(WebSocketCloseListenerImpl.class);

    @Override
    public void process(WebSocket webSocket) {
        logger.error("socket 断开连接");
    }
}
