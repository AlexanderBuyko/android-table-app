package com.demonstration.table.featurehome.dagger

import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import com.demonstration.table.featurehome.ui.home.HomeFragment
import com.demonstration.table.featurehome.ui.reservation.ReservationDialogFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [HomeNavModule::class],
    dependencies = [AppAggregatingProvider::class, ActivityAggregatingProvider::class]
)
interface HomeComponent {

    fun inject(fragment: HomeFragment)

    fun inject(fragment: ReservationDialogFragment)

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