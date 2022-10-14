package com.demonstration.table.featurehome.dagger

import com.demonstration.table.coreapi.CoreMediator
import com.demonstration.table.featurebookingapi.BookingMediator
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object HomeNavModule {

    @Provides
    fun provideBookingMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): BookingMediator {
        return map[BookingMediator::class.java]?.get() as BookingMediator
    }
}