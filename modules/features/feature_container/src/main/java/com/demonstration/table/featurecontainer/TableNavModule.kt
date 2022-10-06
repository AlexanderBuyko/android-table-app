package com.demonstration.table.featurecontainer

import com.demonstration.table.coreapi.CoreMediator
import com.demonstration.table.featurebookingapi.BookingMediator
import com.demonstration.table.featuregreetingapi.GreetingMediator
import com.demonstration.table.featurehomeapi.HomeMediator
import com.demonstration.table.featuresettingsapi.SettingsMediator
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object TableNavModule {

    @Provides
    fun provideGreetingMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): GreetingMediator {
        return map[GreetingMediator::class.java]?.get() as GreetingMediator
    }

    @Provides
    fun provideHomeMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): HomeMediator {
        return map[HomeMediator::class.java]?.get() as HomeMediator
    }

    @Provides
    fun provideBookingMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): BookingMediator {
        return map[BookingMediator::class.java]?.get() as BookingMediator
    }

    @Provides
    fun provideSettingsMediator(map: Map<Class<out CoreMediator>, @JvmSuppressWildcards Provider<Any>>): SettingsMediator {
        return map[SettingsMediator::class.java]?.get() as SettingsMediator
    }
}