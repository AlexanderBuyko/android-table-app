package com.demonstrations.table.featuresettings

import com.demonstration.table.coreapi.CoreMediator
import com.demonstration.table.featuregreetingapi.GreetingMediator
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object SettingsNavModule {

    @Provides
    fun provideGreetingMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): GreetingMediator {
        return map[GreetingMediator::class.java]?.get() as GreetingMediator
    }
}