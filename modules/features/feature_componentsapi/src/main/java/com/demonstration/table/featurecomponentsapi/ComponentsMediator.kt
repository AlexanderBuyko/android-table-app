package com.demonstration.table.featurecomponentsapi

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.demonstration.table.coreapi.CoreMediator

interface ComponentsMediator: CoreMediator {

    fun openComponentsContainerScreen(fragmentManager: FragmentManager, @IdRes container: Int)
}