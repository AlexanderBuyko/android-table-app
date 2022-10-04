package com.demonstration.table.coreapi.holders

import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider

interface ActivityProvidersHolder {

    fun getActivityAggregatingProvider(): ActivityAggregatingProvider
}