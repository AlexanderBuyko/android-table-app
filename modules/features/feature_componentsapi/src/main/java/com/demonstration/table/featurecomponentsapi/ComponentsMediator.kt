package com.demonstration.table.featurecomponentsapi

import androidx.navigation.NavController
import com.demonstration.table.coreapi.CoreMediator

interface ComponentsMediator: CoreMediator {

    fun openComponentsContainerScreen(navController: NavController)
}