package io.tofpu.speedbridge2.command.condition.annotation;

import io.tofpu.speedbridge2.util.material.MaterialCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaterialType {
    MaterialCategory category();
}
