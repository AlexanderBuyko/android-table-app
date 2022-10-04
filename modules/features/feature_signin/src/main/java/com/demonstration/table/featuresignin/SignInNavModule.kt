package com.demonstration.table.featuresignin

import com.demonstration.table.coreapi.CoreMediator
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object SignInNavModule {

    @Provides
    fun provideRegistrationMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): RegistrationMediator {
        return map[RegistrationMediator::class.java]?.get() as RegistrationMediator
    }
}