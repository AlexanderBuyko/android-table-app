package com.demonstration.table.featureregistration

import androidx.fragment.app.FragmentManager
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import javax.inject.Inject

class RegistrationMediatorImpl @Inject constructor() : RegistrationMediator {

    override fun openRegistrationScreen(fragmentManager: FragmentManager, container: Int) {
        fragmentManager
            .beginTransaction()
            .add(container, RegistrationFragment.newInstance())
            .commit()
    }
}