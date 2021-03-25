package com.bomu.game.socket.protobuf.listener;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface WebSocketStartListener {
    Logger logger = LogManager.getLogger(WebSocketStartListener.class);

    default void process() {
        logger.info("当前服务重新启动了。。。");
    }

}
