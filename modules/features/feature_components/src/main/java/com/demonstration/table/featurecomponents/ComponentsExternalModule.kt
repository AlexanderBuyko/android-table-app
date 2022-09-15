package com.demonstration.table.featurecomponents

import com.demonstration.table.featurecomponentsapi.ComponentsMediator
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
interface ComponentsExternalModule {
    @Binds
    @IntoMap
    @ClassKey(ComponentsMediator::class)
    fun putExpensesMediator(tableComponentsMediator: ComponentsMediatorImpl): Any
}