package com.demonstration.table.featurecontainer

import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.providers.activity.NavControllerProvider
import com.demonstration.table.coreapi.scopes.ActivityScope
import dagger.Component

@ActivityScope
@Component(
    modules = [TableNavModule::class],
    dependencies = [AppAggregatingProvider::class, NavControllerProvider::class]
)
interface TableActivityComponent : ActivityAggregatingProvider {

    fun inject(containerActivity: TableActivity)

    @Component.Factory
    interface Factory {
        fun create(
            aggregatingProvider: AppAggregatingProvider,
            navControllerProvider: NavControllerProvider
        ): TableActivityComponent
    }

    companion object {
        fun create(
            aggregatingProvider: AppAggregatingProvider,
            navControllerProvider: NavControllerProvider
        ): TableActivityComponent {
            return DaggerTableActivityComponent
                .factory()
                .create(aggregatingProvider, navControllerProvider)
        }
    }
}
