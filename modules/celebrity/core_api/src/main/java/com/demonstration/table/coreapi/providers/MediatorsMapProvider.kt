package com.demonstration.table.coreapi.providers

import com.demonstration.table.coreapi.CoreMediator
import javax.inject.Provider

interface MediatorsMapProvider {

    fun provideMediatorsMap(): Map<Class<out CoreMediator>,  @JvmSuppressWildcards Provider<Any>>
}