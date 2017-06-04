package com.andreabardella.aifaservicesconsumer.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;
import dagger.MembersInjector;

@MapKey
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SubComponentKey {
    Class<? extends MembersInjector> value();
}