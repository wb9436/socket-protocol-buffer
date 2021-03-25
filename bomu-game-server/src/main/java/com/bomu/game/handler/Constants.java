package com.bomu.game.handler;

import com.bomu.game.socket.config.ApplicationConfig;

/**
 * 游戏服务器常量
 */
public class Constants {

    /**
     * 当前服务器地址
     */
    public static final String MODULE_ADDR = ApplicationConfig.getValue("bomu.websocket.addr");

    /**
     * 解散倒计时
     */
    public static final int APPLY_TIME = Integer.parseInt(ApplicationConfig.getValue("bomu.websocket.applyTime"));

    /**
     * 游戏模块
     */
    public static final String GAME_SYS = ApplicationConfig.getValue("bomu.websocket.appid");

    /**
     * 游戏模块
     */
    public static final String GAME_MODULE = "1000";

}
