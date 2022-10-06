package com.demonstration.table.featureregistration

import com.demonstration.table.coreapi.CoreMediator
import com.demonstration.table.featurehomeapi.HomeMediator
import com.demonstration.table.featuresigninapi.SignInMediator
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object RegistrationNavModule {

    @Provides
    fun provideSignInMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): SignInMediator {
        return map[SignInMediator::class.java]?.get() as SignInMediator
    }

    @Provides
    fun provideHomeMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): HomeMediator {
        return map[HomeMediator::class.java]?.get() as HomeMediator
    }
}