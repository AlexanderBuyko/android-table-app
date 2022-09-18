package com.demonstration.table.featureregistrationapi

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.demonstration.table.coreapi.CoreMediator

interface RegistrationMediator: CoreMediator {

    fun openRegistrationScreen(fragmentManager: FragmentManager, @IdRes container: Int)
}