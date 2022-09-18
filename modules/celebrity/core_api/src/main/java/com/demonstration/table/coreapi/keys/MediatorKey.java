package com.demonstration.table.coreapi.keys;

import com.demonstration.table.coreapi.CoreMediator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import dagger.MapKey;

@MapKey
@Target(ElementType.METHOD)
public @interface MediatorKey {
    Class<? extends CoreMediator> value();
}