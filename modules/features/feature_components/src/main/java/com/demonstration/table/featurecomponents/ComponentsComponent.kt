package com.demonstration.table.featurecomponents

import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(dependencies = [AppAggregatingProvider::class])
interface ComponentsComponent {

    @Component.Factory
    interface Factory {
        fun create(aggregatingProvider: AppAggregatingProvider) : ComponentsComponent
    }

    companion object {
        fun create(aggregatingProvider: AppAggregatingProvider): ComponentsComponent {
            return DaggerComponentsComponent.factory().create(aggregatingProvider)
        }
    }

    fun inject(fragment: ComponentsFragment)
}
