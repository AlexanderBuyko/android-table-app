package com.demonstration.table.featurecontainer

import com.demonstration.table.coreapi.scopes.ActivityScope
import com.demonstration.table.coreapi.providers.AggregatingProvider
import dagger.Component

@ActivityScope
@Component(dependencies = [AggregatingProvider::class], modules = [TableNavModule::class])
interface TableActivityComponent {

    fun inject(containerActivity: TableActivity)

    @Component.Factory
    interface Factory {
        fun create(aggregatingProvider: AggregatingProvider): TableActivityComponent
    }

    companion object {

        fun create(aggregatingProvider: AggregatingProvider): TableActivityComponent {
            return DaggerTableActivityComponent.factory().create(aggregatingProvider)
        }
    }
}
