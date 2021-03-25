package com.bomu.socket.client.notify;

import com.alibaba.fastjson.JSONObject;
import com.bomu.socket.client.WebSocketClient;
import com.bomu.socket.protocol.MetaData;
import com.bomu.socket.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotifyHandler {
    protected final Logger logger = LogManager.getLogger(getClass());

    public static String notifySocketServer(String serverUrl, String sys, String module, String direct, JSONObject data) {
        String result = null;
        try {
            MetaData metaData = new MetaData(sys, module, direct);
            result = WebSocketClient.sendAndClose(serverUrl, new Protocol(metaData, data));
        } catch (Exception e) {
        }
        return result;
    }

}
