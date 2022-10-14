package com.demonstration.table.featureregistration

import com.demonstration.table.coreapi.keys.MediatorKey
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface RegistrationExternalModule {
    @Binds
    @IntoMap
    @MediatorKey(RegistrationMediator::class)
    fun putExpensesMediator(tableComponentsMediator: RegistrationMediatorImpl): Any
}