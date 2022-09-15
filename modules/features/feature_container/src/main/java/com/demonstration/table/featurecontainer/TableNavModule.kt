package com.demonstration.table.featurecontainer

import com.demonstration.table.featurecomponentsapi.ComponentsMediator
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object TableNavModule {

    @Provides
    fun provideComponentsMediator(map: Map<Class<*>, @JvmSuppressWildcards Provider<Any>>): ComponentsMediator {
        return map[ComponentsMediator::class.java]?.get() as ComponentsMediator
    }
}