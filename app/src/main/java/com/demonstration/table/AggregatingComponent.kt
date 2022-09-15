package com.demonstration.table

import com.demonstration.table.featurecomponents.ComponentsExternalModule
import com.demonstration.table.coreapi.providers.AggregatingProvider
import com.demonstration.table.coreapi.providers.CoreProvider
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [ComponentsExternalModule::class],
    dependencies = [CoreProvider::class]
)
@Singleton
interface AggregatingComponent : AggregatingProvider {

    @Component.Factory
    interface Factory {
        fun create(coreProvider: CoreProvider): AggregatingComponent
    }

    companion object {
        fun create(coreProvider: CoreProvider): AggregatingComponent {
            return DaggerAggregatingComponent.factory().create(coreProvider)
        }
    }
}