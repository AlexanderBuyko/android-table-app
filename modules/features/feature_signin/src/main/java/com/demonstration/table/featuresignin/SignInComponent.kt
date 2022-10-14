package com.demonstration.table.featuresignin

import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.scopes.FeatureScope
import com.demonstration.table.featuresignin.recovery.RecoveryFragment
import com.demonstration.table.featuresignin.signin.SignInFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [SignInNavModule::class],
    dependencies = [AppAggregatingProvider::class, ActivityAggregatingProvider::class]
)
interface SignInComponent {

    fun inject(fragment: SignInFragment)

    fun inject(fragment: RecoveryFragment)

    @Component.Factory
    interface Factory {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): SignInComponent
    }

    companion object {
        fun create(
            appAggregatingProvider: AppAggregatingProvider,
            activityAggregatingProvider: ActivityAggregatingProvider
        ): SignInComponent {
            return DaggerSignInComponent
                .factory()
                .create(appAggregatingProvider, activityAggregatingProvider)
        }
    }
}