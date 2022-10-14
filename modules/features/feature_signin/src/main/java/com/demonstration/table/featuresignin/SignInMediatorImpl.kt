package com.demonstration.table.featuresignin

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.demonstration.table.featuresigninapi.SignInMediator
import com.demonstration.baseui.widgets.factories.NavOptionsFactory
import javax.inject.Inject

class SignInMediatorImpl @Inject constructor(
    private val navOptionsFactory: NavOptionsFactory,
) : SignInMediator {

    override fun openSignInScreen(navController: NavController) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("android-app://table.demonstration.com/signin".toUri())
            .build()
        navController.navigate(request, navOptionsFactory.create(R.id.signInFragment))
    }
}