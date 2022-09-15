package com.demonstration.table.coreimpl

import android.content.Context
import com.demonstration.table.coreapi.providers.CoreProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface CoreComponent : CoreProvider {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    companion object {
        fun create(context: Context): CoreProvider {
            return DaggerCoreComponent.factory().create(context)
        }
    }
}