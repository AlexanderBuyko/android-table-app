package com.demonstration.table

import android.app.Application
import com.demonstration.table.coreapi.providers.AggregatingProvider
import com.demonstration.table.coreapi.holders.ProvidersHolder
import com.demonstration.table.corefactory.CelebrityFactory

open class TableApp : Application(), ProvidersHolder {

    companion object {
        private var aggregatingProvider: AggregatingProvider? = null
    }

    override fun onCreate() {
        super.onCreate()
        getAggregatingProvider()
    }

    override fun getAggregatingProvider(): AggregatingProvider {
        return aggregatingProvider
            ?: AggregatingComponent.create(coreProvider = CelebrityFactory.createCoreProvider(this))
                .also { aggregatingProvider = it }
    }
}