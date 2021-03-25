package com.bomu.game.socket.annotation;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Direct {
    String value() default "";
}
