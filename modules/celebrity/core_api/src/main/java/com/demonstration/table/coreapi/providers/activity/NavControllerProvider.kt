package com.demonstration.table.coreapi.providers.activity

import androidx.navigation.NavController

interface NavControllerProvider {
    fun getNavController(): NavController
}