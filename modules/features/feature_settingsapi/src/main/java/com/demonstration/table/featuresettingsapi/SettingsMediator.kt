package com.demonstration.table.featuresettingsapi

import androidx.navigation.NavController
import com.demonstration.table.coreapi.CoreMediator

interface SettingsMediator : CoreMediator {

    fun openSettingsScreen(navController: NavController)
}