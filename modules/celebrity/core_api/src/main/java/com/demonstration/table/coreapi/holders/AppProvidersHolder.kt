package com.demonstration.table.coreapi.holders

import com.demonstration.table.coreapi.providers.application.AppAggregatingProvider

interface AppProvidersHolder {

    fun getAggregatingProvider(): AppAggregatingProvider
}