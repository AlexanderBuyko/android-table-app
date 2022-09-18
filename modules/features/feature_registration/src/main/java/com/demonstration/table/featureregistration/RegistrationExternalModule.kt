package com.demonstration.table.featureregistration

import com.demonstration.table.featureregistrationapi.RegistrationMediator
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface RegistrationExternalModule {
    @Binds
    @IntoMap
    @ClassKey(RegistrationMediator::class)
    fun putExpensesMediator(tableComponentsMediator: RegistrationMediatorImpl): Any
}