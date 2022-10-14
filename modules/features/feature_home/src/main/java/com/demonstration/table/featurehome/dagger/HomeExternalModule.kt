package com.demonstration.table.featurehome.dagger

import com.demonstration.table.coreapi.keys.MediatorKey
import com.demonstration.table.featurehome.HomeMediatorImpl
import com.demonstration.table.featurehomeapi.HomeMediator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface HomeExternalModule {
    @Binds
    @IntoMap
    @MediatorKey(HomeMediator::class)
    fun putHomeMediator(greetingMediator: HomeMediatorImpl): Any
}