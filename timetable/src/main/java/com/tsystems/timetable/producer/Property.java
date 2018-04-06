package com.tsystems.timetable.producer;


import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
// Defines method / variable that should be injected with value read from some arbitrary resource
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface Property {
    //Key that will be searched when injecting the value.
    @Nonbinding String value() default "";
    //Defines if value for the given key must be defined.
    @Nonbinding boolean required() default true;
}