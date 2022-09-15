package com.demonstration.table.featurecomponentsapi

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface ComponentsMediator {

    fun openComponentsContainerScreen(fragmentManager: FragmentManager, @IdRes container: Int)
}