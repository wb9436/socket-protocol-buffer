package com.bomu.game.listener;


import com.bomu.game.socket.protobuf.listener.WebSocketStartListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class WebSocketStartListenerImpl implements WebSocketStartListener {
    private static final Logger logger = LogManager.getLogger(WebSocketStartListenerImpl.class);

    @Override
    public void process() {
        logger.info("Protocol Buffer 测试服务重新启动了。。。");
    }

}
