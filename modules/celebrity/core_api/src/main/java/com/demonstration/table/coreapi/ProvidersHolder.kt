package com.demonstration.table.coreapi

import com.demonstration.table.coreapi.providers.AggregatingProvider

interface ProvidersHolder {

    fun getAggregatingProvider(): AggregatingProvider
}