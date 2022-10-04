package com.demonstration.table.featuregreeting

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import com.demonstration.table.featuregreetingapi.GreetingMediator
import com.example.baseui.R as baseR
import javax.inject.Inject

class GreetingMediatorImpl @Inject constructor() : GreetingMediator {

    override fun openGreetingScreen(navController: NavController) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(R.id.greetingFragment, true)
            .setEnterAnim(baseR.anim.slide_in_right)
            .setExitAnim(baseR.anim.slide_out_left)
            .setPopEnterAnim(baseR.anim.slide_in_left)
            .setPopExitAnim(baseR.anim.slide_out_right)
            .build()

        val linkRequest = NavDeepLinkRequest.Builder
            .fromUri("android-app://table.demonstration.com/greeting".toUri())
            .build()

        navController.navigate(linkRequest, navOptions)
    }
}