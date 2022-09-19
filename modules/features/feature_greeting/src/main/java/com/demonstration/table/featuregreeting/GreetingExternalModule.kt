package com.demonstration.table.featuregreeting

import com.demonstration.table.coreapi.keys.MediatorKey
import com.demonstration.table.featuregreetingapi.GreetingMediator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface GreetingExternalModule {
    @Binds
    @IntoMap
    @MediatorKey(GreetingMediator::class)
    fun putGreetingMediator(greetingMediator: GreetingMediatorImpl): Any
}