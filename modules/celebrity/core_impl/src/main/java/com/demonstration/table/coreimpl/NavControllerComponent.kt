package com.demonstration.table.coreimpl

import androidx.navigation.NavController
import com.demonstration.table.coreapi.providers.activity.NavControllerProvider
import com.demonstration.table.coreapi.scopes.ActivityScope
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component
interface NavControllerComponent: NavControllerProvider {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance navController: NavController): NavControllerComponent
    }

    companion object {
        fun create(navController: NavController): NavControllerComponent {
            return DaggerNavControllerComponent.factory().create(navController)
        }
    }
}