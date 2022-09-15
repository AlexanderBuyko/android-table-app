package com.demonstration.table.featurecomponents

import com.demonstration.table.featurecomponents.DaggerComponentsComponent
import com.demonstration.table.coreapi.providers.AggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(dependencies = [AggregatingProvider::class])
interface ComponentsComponent {

    @Component.Factory
    interface Factory {
        fun create(aggregatingProvider: AggregatingProvider) : ComponentsComponent
    }

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): ComponentsComponent {
            return DaggerComponentsComponent.factory().create(aggregatingProvider)
        }
    }

    fun inject(fragment: ComponentsFragment)
}
