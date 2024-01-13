package com.github.tofpu.speedbridge2.event.dispatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
    ListeningState state() default ListeningState.LISTENING;
    boolean ignoreCancelled() default false;
}
