package com.demonstration.table.featurebooking

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.demonstration.table.featurebookingapi.BookingMediator
import javax.inject.Inject

class BookingMediatorImpl @Inject constructor() : BookingMediator {

    override fun openBookingScreen(navController: NavController) {
        val linkRequest = NavDeepLinkRequest.Builder
            .fromUri("android-app://table.demonstration.com/booking".toUri())
            .build()
        navController.navigate(linkRequest)
    }
}