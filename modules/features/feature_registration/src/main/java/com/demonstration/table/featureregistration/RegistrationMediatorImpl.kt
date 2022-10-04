package com.demonstration.table.featureregistration

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import com.example.baseui.factories.NavOptionsFactory
import javax.inject.Inject

class RegistrationMediatorImpl @Inject constructor(
    private val navOptionsFactory: NavOptionsFactory
) : RegistrationMediator {

    override fun openRegistrationScreen(navController: NavController) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("android-app://table.demonstration.com/registration".toUri())
            .build()
        navController.navigate(request, navOptionsFactory.create())
    }
}