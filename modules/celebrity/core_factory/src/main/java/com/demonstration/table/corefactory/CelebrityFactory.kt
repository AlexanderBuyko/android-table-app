package com.demonstration.table.corefactory

import android.content.Context
import com.demonstration.table.coreapi.providers.CoreProvider
import com.demonstration.table.coreimpl.CoreComponent

object CelebrityFactory {

    fun createCoreProvider(context: Context): CoreProvider {
        return CoreComponent.create(context)
    }
}