package com.bomu.game.socket.annotation;

import com.bomu.game.socket.config.WSConfig;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnnotationUtils {

    private AnnotationUtils() {
    }

    @Autowired
    private WSConfig config;
    private static AnnotationUtils instance = new AnnotationUtils();
    private static Map<String, Class> map = new ConcurrentHashMap<String, Class>();

    @PostConstruct
    public void init() {
        long s = System.currentTimeMillis();
        Reflections reflections = new Reflections("com.bomu.game");
        Set<Class<?>> classList = reflections.getTypesAnnotatedWith(WebSocketAction.class);
        for (Class cls : classList) {
            WebSocketAction acton = (WebSocketAction) cls.getAnnotation(WebSocketAction.class);
            String key = getSysModule(config.getAppid(),acton.module());
            if (!map.containsKey(key)) {
                map.put(key, cls);
            } else {
                throw new RuntimeException("重复加载Action[" + acton.module() + "]");
            }
        }
    }

    public String getSysModule(String sys,String module) {
        StringBuffer sb = new StringBuffer();
        sb.append(sys).append(module);
        return sb.toString();
    }

    public Class getActionClass(String name) {
        return map.get(name);
    }

    public static AnnotationUtils getInstance() {
        return instance;
    }
}
