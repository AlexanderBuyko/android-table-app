package com.demonstration.table.featuregreeting

import com.demonstration.table.coreapi.providers.AggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(modules = [GreetingNavModule::class], dependencies = [AggregatingProvider::class])
interface GreetingComponent {

    fun inject(fragment: GreetingFragment)

    @Component.Factory
    interface Factory {
        fun create(aggregatingProvider: AggregatingProvider) : GreetingComponent
    }

    companion object {
        fun create(aggregatingProvider: AggregatingProvider): GreetingComponent {
            return DaggerGreetingComponent.factory().create(aggregatingProvider)
        }
    }
}