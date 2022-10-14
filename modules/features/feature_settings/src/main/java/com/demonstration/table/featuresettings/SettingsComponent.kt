package com.demonstration.table.featuresettings

import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [SettingsNavModule::class],
    dependencies = [AppAggregatingProvider::class, ActivityAggregatingProvider::class]
)
interface SettingsComponent {

    fun inject(fragment: SettingsFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): SettingsComponent
    }

    companion object {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): SettingsComponent {
            return DaggerSettingsComponent.factory()
                .create(appAggregatingProvider, activityAggregatingProvider)
        }
    }
}