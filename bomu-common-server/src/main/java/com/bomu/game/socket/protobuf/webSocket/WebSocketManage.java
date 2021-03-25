package com.bomu.game.socket.protobuf.webSocket;

import com.bomu.socket.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guiyuan
 */
public class WebSocketManage {

    private static final Logger logger = LogManager.getLogger(WebSocketManage.class);

    /**
     * 已绑定用户的webSocket集合
     */
    private static final ConcurrentHashMap<String, WebSocket> bindWebSockets = new ConcurrentHashMap<String, WebSocket>();

    private static String userIdKey(Integer userId) {
        return userId.toString();
    }

    /**
     * 将WebSocket 与用户绑定
     *
     * @param userId
     * @param webSocket
     */
    public static void bind(Integer userId, String areaId, WebSocket webSocket) {
        if (userId == null) {
            throw new RuntimeException("userId is NULL.");
        }
        WebSocket oldSocket = getWebSocket(webSocket.getUserId());
        if (oldSocket != null && !webSocket.getId().equals(oldSocket.getId())) {
            oldSocket.close();
        }
        String userIdKey = userIdKey(userId);
        webSocket.setUserId(userId);
        webSocket.setAreaId(areaId);
        bindWebSockets.put(userIdKey, webSocket);
    }

    /***
     * 从集合中移除WebSocket
     * @param webSocket
     */
    public static void remove(WebSocket webSocket) {
        if (webSocket == null) {
            logger.info("websocket is not exist or is NULL");
            return;
        }
        if (webSocket.getUserId() == null) {
            logger.debug("Not found current websocket,and websocket id is:" + webSocket.getId());
            return;
        }
        //判断被删除的socket与已缓存的socket是否相同
        WebSocket newSocket = getWebSocket(webSocket.getUserId());
        if (newSocket != null && webSocket.getId().equals(newSocket.getId())) {
            String userIdKey = userIdKey(webSocket.getUserId());
            bindWebSockets.remove(userIdKey);
        }
    }

    /**
     * 获取用户绑定的WebSocket
     *
     * @param userId
     * @return
     */
    public static WebSocket getWebSocket(Integer userId) {
        String userIdKey = userIdKey(userId);
        WebSocket socket = bindWebSockets.get(userIdKey);
        if (socket == null) {
            logger.warn("Not found the websocket, and userId is " + userId);
        }
        return socket;
    }

    /**
     * 将消息推送至所有绑定的用户的客户端
     *
     * @param protocal
     */
    public static void broadcast(Protocol protocal) {
        Collection<WebSocket> colls = bindWebSockets.values();
        for (Iterator<WebSocket> it = colls.iterator(); it.hasNext(); ) {
            WebSocket webSocket = it.next();
            webSocket.send(protocal);
        }
    }

    /**
     * 将消息推送至所有绑定的用户的客户端
     *
     * @param webSocket0 不需要发送消息的WebSocket
     * @param protocal
     */
    public static void broadcast(WebSocket webSocket0, Protocol protocal) {
        Collection<WebSocket> colls = bindWebSockets.values();
        for (Iterator<WebSocket> it = colls.iterator(); it.hasNext(); ) {
            WebSocket webSocket = it.next();
            if (webSocket.equals(webSocket0)) {
                continue;
            }
            if (webSocket.getUserId() != null) {
                webSocket.send(protocal);
            }
        }
    }

    /**
     * 给指定用户发送消息
     *
     * @param userId
     * @param protocal
     */
    public static void send(Integer userId, Protocol protocal) {
        WebSocket webSocket = getWebSocket(userId);
        if (webSocket != null) {
            webSocket.send(protocal);
        }
    }

    /**
     * 关闭websocket
     *
     * @param userId
     */
    public static void close(Integer userId) {
        WebSocket webSocket = getWebSocket(userId);
        if (webSocket != null) {
            webSocket.close();
        } else {
            logger.warn("当前用户已经下线" + Integer.toString(userId));
        }
    }

    /**
     * 获取在线列表
     *
     * @return
     */
    public static Collection<WebSocket> getWebSockets() {
        return bindWebSockets.values();
    }

    /**
     * 获取在线用户数量
     */
    public static int getOnlineCount() {
        return bindWebSockets.size();
    }
}
