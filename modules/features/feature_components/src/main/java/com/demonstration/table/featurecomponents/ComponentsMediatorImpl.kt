package com.demonstration.table.featurecomponents

import androidx.fragment.app.FragmentManager
import com.demonstration.table.featurecomponentsapi.ComponentsMediator
import javax.inject.Inject

class ComponentsMediatorImpl @Inject constructor() : ComponentsMediator {

    override fun openComponentsContainerScreen(fragmentManager: FragmentManager, container: Int) {
        fragmentManager
            .beginTransaction()
            .add(container, ComponentsFragment.newInstance())
            .commit()
    }
}