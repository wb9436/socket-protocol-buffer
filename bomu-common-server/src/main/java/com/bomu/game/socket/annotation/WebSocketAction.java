package com.bomu.game.socket.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface WebSocketAction {

    String value() default "";

    String module() ;

}
