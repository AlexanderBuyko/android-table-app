package com.demonstration.table.coreapi.providers

import javax.inject.Provider

interface MediatorsMapProvider {

    fun provideMediatorsMap(): Map<Class<*>,  @JvmSuppressWildcards Provider<Any>>
}