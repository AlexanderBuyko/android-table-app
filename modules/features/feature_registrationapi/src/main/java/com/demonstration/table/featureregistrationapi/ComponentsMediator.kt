package com.demonstration.table.featureregistrationapi

import androidx.navigation.NavController
import com.demonstration.table.coreapi.CoreMediator

interface RegistrationMediator: CoreMediator {

    fun openRegistrationScreen(navController: NavController)
}