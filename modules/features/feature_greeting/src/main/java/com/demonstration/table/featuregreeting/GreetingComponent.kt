package com.demonstration.table.featuregreeting

import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [GreetingNavModule::class],
    dependencies = [AppAggregatingProvider::class, ActivityAggregatingProvider::class]
)
interface GreetingComponent {

    fun inject(fragment: GreetingFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): GreetingComponent
    }

    companion object {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): GreetingComponent {
            return DaggerGreetingComponent.factory().create(appAggregatingProvider, activityAggregatingProvider)
        }
    }
}