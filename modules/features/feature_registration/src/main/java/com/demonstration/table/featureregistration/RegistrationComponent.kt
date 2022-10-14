package com.demonstration.table.featureregistration

import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [RegistrationNavModule::class],
    dependencies = [AppAggregatingProvider::class, ActivityAggregatingProvider::class]
)
interface RegistrationComponent {

    fun inject(fragment: RegistrationFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): RegistrationComponent
    }

    companion object {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): RegistrationComponent {
            return DaggerRegistrationComponent.factory()
                .create(appAggregatingProvider, activityAggregatingProvider)
        }
    }
}