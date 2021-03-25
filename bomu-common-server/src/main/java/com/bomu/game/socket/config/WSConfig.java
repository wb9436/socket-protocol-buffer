package com.bomu.game.socket.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class WSConfig implements ApplicationContextAware {

    public static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        env = applicationContext.getBean(Environment.class);
    }

    public static int getPort() {
        return env.getProperty("bomu.websocket.port", Integer.class);
    }
    public static int getBacklog(){
        return env.getProperty("bomu.websocket.backlog", Integer.class);
    }
    public static boolean isKeeplive(){
        return env.getProperty("bomu.websocket.keeplive", Boolean.class, true);
    }

    public static int getReadIdleTimeSeconds(){
        return env.getProperty("bomu.websocket.readIdleTimeSeconds", int.class);
    }

    public static int getWriteIdleTimeSeconds(){
        return env.getProperty("bomu.websocket.writeIdleTimeSeconds", int.class);
    }

    public static int getIdleTimeSeconds(){
        return env.getProperty("bomu.websocket.idleTimeSeconds",int.class);
    }

    public static String getAppid() {
        return env.getProperty("bomu.websocket.appid");
    }

    public static String getWebsocketPath(){
        return env.getProperty("bomu.websocket.websocketPath");
    }

}
