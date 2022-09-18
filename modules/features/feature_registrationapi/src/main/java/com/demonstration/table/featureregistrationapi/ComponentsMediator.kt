package com.demonstration.table.featureregistrationapi

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager

interface RegistrationMediator {

    fun openRegistrationScreen(fragmentManager: FragmentManager, @IdRes container: Int)
}