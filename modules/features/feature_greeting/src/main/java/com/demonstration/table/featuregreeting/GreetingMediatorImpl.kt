package com.demonstration.table.featuregreeting

import androidx.fragment.app.FragmentManager
import com.demonstration.table.featuregreetingapi.GreetingMediator
import javax.inject.Inject

class GreetingMediatorImpl @Inject constructor() : GreetingMediator {
    override fun openGreetingScreen(fragmentManager: FragmentManager, container: Int) {
        fragmentManager
            .beginTransaction()
            .add(container, GreetingFragment.newInstance())
            .commit()
    }
}