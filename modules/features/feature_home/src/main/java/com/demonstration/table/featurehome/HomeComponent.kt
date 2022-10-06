package com.demonstration.table.featurehome

import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [HomeNavModule::class],
    dependencies = [AppAggregatingProvider::class, ActivityAggregatingProvider::class]
)
interface HomeComponent {

    fun inject(fragment: HomeFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): HomeComponent
    }

    companion object {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): HomeComponent {
            return DaggerHomeComponent.factory()
                .create(appAggregatingProvider, activityAggregatingProvider)
        }
    }
}