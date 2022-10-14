package com.demonstration.table.featurebooking

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.demonstration.baseui.widgets.factories.NavOptionsFactory
import com.demonstration.table.featurebookingapi.BookingMediator
import javax.inject.Inject

class BookingMediatorImpl @Inject constructor(
    private val navOptionsFactory: NavOptionsFactory
) : BookingMediator {

    private val linkRequest = NavDeepLinkRequest.Builder
        .fromUri("android-app://table.demonstration.com/booking".toUri())
        .build()

    override fun openBookingScreen(navController: NavController) {
        navController.navigate(linkRequest)
    }

    override fun openBookingScreenWithSlideAnim(navController: NavController) {
        navController.navigate(linkRequest, navOptionsFactory.createDefault())
    }
}