package com.demonstration.table.featuresettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featuregreetingapi.GreetingMediator
import com.demonstration.table.basetable.base.BaseFragment
import com.demonstration.baseui.widgets.extentions.setSafeOnClickListener
import com.demonstration.baseui.widgets.extentions.updateTopMarginOnApplyWindowInsets
import com.demonstration.table.featuresettings.databinding.FragmentSettingsBinding
import javax.inject.Inject

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var greetingMediator: GreetingMediator

    @Inject
    lateinit var navController: NavController

    private lateinit var viewModel: SettingsViewModel

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentSettingsBinding.inflate(inflater, parent, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDaggerComponent()
        initViewModel()
    }

    private fun initDaggerComponent() {
        SettingsComponent.create(
            (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
            (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider(),
        ).also {
            it.inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            title.updateTopMarginOnApplyWindowInsets()
            logOutTitle.setSafeOnClickListener {
                greetingMediator.openGreetingScreen(navController)
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }
}