package com.demonstration.table

import android.app.Application
import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.coreapi.providers.application.CoreProvider
import com.demonstration.table.coreapi.providers.application.models.NavigationId
import com.demonstration.table.corefactory.CelebrityFactory
import com.jakewharton.threetenabp.AndroidThreeTen

open class TableApp : Application(), AppProvidersHolder {

    private val navigationId: NavigationId
        get() = NavigationId(R.navigation.nav_graph)

    private val coreProvider: CoreProvider
        get() = CelebrityFactory.createCoreProvider(context = this, navigationId = navigationId)

    override fun onCreate() {
        super.onCreate()
        getAggregatingProvider()

        initThreeTenAdb()
    }

    private fun initThreeTenAdb() {
        AndroidThreeTen.init(this)
    }

    override fun getAggregatingProvider(): AppAggregatingProvider {
        return aggregatingProvider
            ?: TableAppComponent
                .create(coreProvider = coreProvider)
                .also { aggregatingProvider = it }
    }

    companion object {
        private var aggregatingProvider: AppAggregatingProvider? = null
    }
}