package io.automationhacks.testinfra.attribution.annotations;

import io.automationhacks.testinfra.constants.Oncalls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface OnCall {
    Oncalls value();
}
