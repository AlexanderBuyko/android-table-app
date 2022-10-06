package com.demonstrations.table.featuresettings

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.demonstration.table.featuresettingsapi.SettingsMediator
import javax.inject.Inject

class SettingsMediatorImpl @Inject constructor() : SettingsMediator {
    override fun openSettingsScreen(navController: NavController) {
        val linkRequest = NavDeepLinkRequest.Builder
            .fromUri("android-app://table.demonstration.com/settings".toUri())
            .build()
        navController.navigate(linkRequest)
    }
}