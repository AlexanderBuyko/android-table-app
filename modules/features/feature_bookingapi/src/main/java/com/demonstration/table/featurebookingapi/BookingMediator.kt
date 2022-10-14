package com.demonstration.table.featurebookingapi

import androidx.navigation.NavController
import com.demonstration.table.coreapi.CoreMediator

interface BookingMediator: CoreMediator {
    fun openBookingScreen(navController: NavController)

    fun openBookingScreenWithSlideAnim(navController: NavController)
}