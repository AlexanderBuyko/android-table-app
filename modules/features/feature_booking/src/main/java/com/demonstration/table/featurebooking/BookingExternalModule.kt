package com.demonstration.table.featurebooking

import com.demonstration.table.coreapi.keys.MediatorKey
import com.demonstration.table.featurebookingapi.BookingMediator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface BookingExternalModule {
    @Binds
    @IntoMap
    @MediatorKey(BookingMediator::class)
    fun putBookingMediator(greetingMediator: BookingMediatorImpl): Any
}