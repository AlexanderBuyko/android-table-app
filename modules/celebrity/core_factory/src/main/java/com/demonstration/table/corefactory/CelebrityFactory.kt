package com.demonstration.table.corefactory

import android.content.Context
import androidx.navigation.NavController
import com.demonstration.table.coreapi.providers.activity.NavControllerProvider
import com.demonstration.table.coreapi.providers.application.CoreProvider
import com.demonstration.table.coreapi.providers.application.models.NavigationId
import com.demonstration.table.coreimpl.CoreComponent
import com.demonstration.table.coreimpl.NavControllerComponent

object CelebrityFactory {

    fun createCoreProvider(context: Context, navigationId: NavigationId): CoreProvider {
        return CoreComponent.create(context, navigationId)
    }

    fun createNavControllerProvider(navController: NavController): NavControllerProvider {
        return NavControllerComponent.create(navController)
    }
}