package com.demonstration.table.coreapi.providers.application

import android.content.Context
import com.demonstration.table.coreapi.providers.application.models.NavigationId

interface CoreProvider {

    fun provideContext(): Context

    fun provideNavigationId(): NavigationId
}