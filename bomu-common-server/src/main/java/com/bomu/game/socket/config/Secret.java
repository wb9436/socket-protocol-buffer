package com.bomu.game.socket.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class Secret implements ApplicationContextAware {
    public static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        env = applicationContext.getBean(Environment.class);
    }

    public static String getAES256Key(){
        return env.getProperty("bomu.secret.aes256Key");
    }

    public static boolean isCipher(){
        return env.getProperty("bomu.secret.cipher",Boolean.class,true);
    }
}
