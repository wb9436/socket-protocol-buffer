package com.bomu.game.socket.protobuf.dispatcher;

import com.alibaba.fastjson.JSONObject;
import com.bomu.game.socket.annotation.AnnotationUtils;
import com.bomu.game.socket.annotation.Direct;
import com.bomu.game.socket.config.SpringContext;
import com.bomu.game.socket.protobuf.webSocket.WebSocket;
import com.bomu.socket.protocol.MetaData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import protocol.MetaDataBase;

import java.lang.reflect.Method;

@Component
public class Dispatcher {
    private static final Logger logger = LogManager.getLogger(Dispatcher.class);

    public static void dispatcher(WebSocket webSocket, MetaDataBase.MetaData metaData) {
        MetaData meta = new MetaData(metaData);

        String text = metaData.getData();
        JSONObject data = JSONObject.parseObject(text);

//        //校验token
//        boolean tokenIsRight = GToken.verify(meta.getGToken());
//        if (!GameConfig.hasInWhiteList(webSocket.getIp())) {
//            if (!tokenIsRight) {
//                logger.error("GToken 校验不通过: " + meta.getGToken() + "; webSocket：" + webSocket.toString()+ "; meta=" + meta.toString() + "; data=" + data.toJSONString());
//                webSocket.close();
//                return;
//            }
//            String userId = GToken.getUserId(meta.getGToken());
//            String areaId = GToken.getAreaId(meta.getGToken());
//            if (StringUtils.isTrimEmpty(userId) || StringUtils.isTrimEmpty(areaId)) {
//                logger.error("GToken 数据异常: " + meta.getGToken() + "; webSocket：" + webSocket.toString()+ "; meta=" + meta.toString() + "; data=" + data.toJSONString());
//                webSocket.close();
//                return;
//            }
//        }
//        if (tokenIsRight) {
//            String userId = GToken.getUserId(meta.getGToken());
//            String areaId = GToken.getAreaId(meta.getGToken());
//            if (!StringUtils.isTrimEmpty(userId) && !StringUtils.isTrimEmpty(areaId)) {
//                if (webSocket.getUserId() == null) {
//                    int newUserId = Integer.parseInt(userId);
//                    webSocket.setUserId(newUserId);
//                    WebSocketManage.bind(newUserId, areaId, webSocket);
//                }
//            }
//        }
        Class cls = AnnotationUtils.getInstance().getActionClass(meta.getSysModule());
        if (cls != null) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                Direct direct = method.getAnnotation(Direct.class);
                if (direct != null && direct.value().equals(meta.getDirect())) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes != null && paramTypes.length == 3) {
                        if (paramTypes[0] == WebSocket.class &&
                                paramTypes[1] == MetaData.class &&
                                paramTypes[2] == JSONObject.class) {
                            Object o = SpringContext.getBean(cls);
                            try {
                                if (!"998".equals(meta.getDirect())) { //心跳不打印
                                    logger.info("Socket请求: webSocket=" + webSocket.toString() + "; meta=" + meta.toString() + "; data=" + data.toJSONString());
                                }
                                method.invoke(o, new Object[]{webSocket, meta, data});
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }

                }
            }
        } else {
            logger.warn("Not found this direct:" + meta.getSysModule());
        }
    }
}
