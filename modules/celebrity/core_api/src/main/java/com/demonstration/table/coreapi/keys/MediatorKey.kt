package com.demonstration.table.coreapi.keys

import com.demonstration.table.coreapi.CoreMediator
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class MediatorKey(
    val values: KClass<out CoreMediator>
)