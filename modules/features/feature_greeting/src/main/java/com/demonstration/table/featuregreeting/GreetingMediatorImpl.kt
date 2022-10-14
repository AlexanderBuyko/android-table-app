package com.demonstration.table.featuregreeting

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.demonstration.table.featuregreetingapi.GreetingMediator
import com.demonstration.baseui.widgets.factories.NavOptionsFactory
import javax.inject.Inject

class GreetingMediatorImpl @Inject constructor(
    private val navOptionsFactory: NavOptionsFactory
) : GreetingMediator {

    override fun openGreetingScreen(navController: NavController) {
        while (navController.popBackStack()) continue
        val linkRequest = NavDeepLinkRequest.Builder
            .fromUri("android-app://table.demonstration.com/greeting".toUri())
            .build()

        navController.navigate(linkRequest, navOptionsFactory.createDefault())
    }
}