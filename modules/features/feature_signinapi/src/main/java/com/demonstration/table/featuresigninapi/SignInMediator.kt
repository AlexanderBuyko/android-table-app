package com.demonstration.table.featuresigninapi

import androidx.navigation.NavController
import com.demonstration.table.coreapi.CoreMediator

interface SignInMediator: CoreMediator {

    fun openSignInScreen(navController: NavController)
}