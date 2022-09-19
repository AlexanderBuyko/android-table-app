package com.demonstration.table.featuregreetingapi

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.demonstration.table.coreapi.CoreMediator

interface GreetingMediator: CoreMediator {

    fun openGreetingScreen(fragmentManager: FragmentManager, @IdRes container: Int)
}