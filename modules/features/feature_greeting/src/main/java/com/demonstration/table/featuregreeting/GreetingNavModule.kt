package com.demonstration.table.featuregreeting

import com.demonstration.table.coreapi.CoreMediator
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object GreetingNavModule {

    @Provides
    fun provideRegistrationMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): RegistrationMediator {
        return map[RegistrationMediator::class.java]?.get() as RegistrationMediator
    }
}