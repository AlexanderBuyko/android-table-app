package com.demonstration.table.featurehomeapi

import androidx.navigation.NavController
import com.demonstration.table.coreapi.CoreMediator

interface HomeMediator : CoreMediator {

    fun openHomeScreen(navController: NavController)
}