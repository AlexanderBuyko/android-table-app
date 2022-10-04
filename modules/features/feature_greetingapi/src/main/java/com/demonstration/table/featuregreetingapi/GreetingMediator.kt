package com.demonstration.table.featuregreetingapi

import androidx.navigation.NavController
import com.demonstration.table.coreapi.CoreMediator

interface GreetingMediator: CoreMediator {

    fun openGreetingScreen(navController: NavController)
}