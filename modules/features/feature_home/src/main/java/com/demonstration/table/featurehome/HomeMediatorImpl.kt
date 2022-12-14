package com.demonstration.table.featurehome

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.demonstration.table.featurehomeapi.HomeMediator
import javax.inject.Inject

class HomeMediatorImpl @Inject constructor() : HomeMediator {
    override fun openHomeScreen(navController: NavController) {
        while (navController.popBackStack()) continue
        val linkRequest = NavDeepLinkRequest.Builder
            .fromUri("android-app://table.demonstration.com/home".toUri())
            .build()
        navController.navigate(linkRequest)
    }
}