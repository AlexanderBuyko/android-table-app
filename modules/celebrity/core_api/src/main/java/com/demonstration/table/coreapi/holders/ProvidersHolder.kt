package com.demonstration.table.coreapi.holders

import com.demonstration.table.coreapi.providers.AggregatingProvider

interface ProvidersHolder {

    fun getAggregatingProvider(): AggregatingProvider
}