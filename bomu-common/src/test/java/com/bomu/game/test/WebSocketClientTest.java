package com.bomu.game.test;

import com.alibaba.fastjson.JSONObject;
import com.bomu.socket.client.notify.NotifyHandler;

public class WebSocketClientTest {

    public static void main(String[] args) {
        String addr = "ws://192.168.1.166:18113/protocol";
        String sys = "1000";
        String module = "1000";
        String direct = "1000";

        JSONObject data = new JSONObject();
        data.put("abc", 1111);

        String result = NotifyHandler.notifySocketServer(addr, sys, module, direct, data);
        System.out.println(result != null ? ("返回结果: " + result) : "通知返回异常");
    }
}
