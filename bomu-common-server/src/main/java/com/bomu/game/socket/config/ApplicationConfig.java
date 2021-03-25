package com.bomu.game.socket.config;

import com.bomu.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 方便静态引用application.properties的参数
 * Autowired annotation is not supported on static fields
 *
 * @author WB
 */
@Configuration
public class ApplicationConfig implements ApplicationContextAware {
    private static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        env = applicationContext.getBean(Environment.class);
    }

    public static String getValue(String key) {
        return env.getProperty(key);
    }

    /**
     * 图片预览地址
     *
     * @return
     */
    public static String getImageVisitPath() {
        return getValue("bomu.upload.visit");
    }


    /**
     * 榜单统计起始时间点
     */
    public static String getListStartPoint() {
        String startPoint = getValue("bomu.listTime.startPoint");
        if (StringUtils.isTrimEmpty(startPoint)) {
            startPoint = "00:00:00";
        }
        return startPoint.trim();
    }


    /**
     * 榜单统计结束时间点
     */
    public static String getListEndPoint() {
        String endPoint = getValue("bomu.listTime.endPoint");
        if (StringUtils.isTrimEmpty(endPoint)) {
            endPoint = "00:00:00";
        }
        return endPoint.trim();
    }


    /**
     * Rpc 服务IP
     */
    public static String getRpcIp() {
        return getValue("bomu.rpc.ip");
    }

    /**
     * Rpc 服务端口
     */
    public static int getRpcPort() {
        String portStr = getValue("bomu.rpc.port");
        int port = 0;
        try {
            port = Integer.parseInt(portStr.trim());
        } catch (Exception e) {
        }
        return port;
    }

}
