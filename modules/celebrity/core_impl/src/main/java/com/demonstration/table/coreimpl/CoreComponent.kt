package com.demonstration.table.coreimpl

import android.content.Context
import com.demonstration.table.coreapi.providers.application.CoreProvider
import com.demonstration.table.coreapi.providers.application.models.NavigationId
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface CoreComponent : CoreProvider {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance navigationId: NavigationId,
        ): CoreComponent
    }

    companion object {
        fun create(context: Context, navigationId: NavigationId): CoreProvider {
            return DaggerCoreComponent.factory().create(context, navigationId)
        }
    }
}