package com.bomu.game.socket.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Bean管理器
 *
 * @WB
 */
@Component
public class SpringContext implements ApplicationContextAware {
    private static Logger logger = LogManager.getLogger(SpringContext.class);

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug("ApplicationContext registed-->{}", applicationContext);
        context = applicationContext;
    }

    /**
     * 获取容器
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 获取容器对象
     *
     * @param beanName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 获取容器对象
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }
}
