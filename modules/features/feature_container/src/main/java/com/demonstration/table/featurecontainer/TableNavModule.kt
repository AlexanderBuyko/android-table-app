package com.demonstration.table.featurecontainer

import com.demonstration.table.coreapi.CoreMediator
import com.demonstration.table.featurecomponentsapi.ComponentsMediator
import com.demonstration.table.featuregreetingapi.GreetingMediator
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object TableNavModule {

    @Provides
    fun provideComponentsMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): ComponentsMediator {
        return map[ComponentsMediator::class.java]?.get() as ComponentsMediator
    }

    @Provides
    fun provideRegistrationMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): RegistrationMediator {
        return map[RegistrationMediator::class.java]?.get() as RegistrationMediator
    }

    @Provides
    fun provideGreetingMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): GreetingMediator {
        return map[GreetingMediator::class.java]?.get() as GreetingMediator
    }
}