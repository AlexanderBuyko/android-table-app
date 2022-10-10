package com.demonstration.table.featurehome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featurehome.R
import com.demonstration.table.featurehome.dagger.HomeComponent
import com.demonstration.table.featurehome.databinding.FragmentHomeBinding
import com.demostration.table.basetable.base.BaseFragment
import com.example.baseui.extentions.setSafeOnClickListener
import com.example.baseui.extentions.updateTopMarginOnApplyWindowInsets
import com.example.baseui.factories.NavOptionsFactory
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var navController: NavController

    @Inject
    lateinit var navOptionsFactory: NavOptionsFactory

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentHomeBinding.inflate(inflater, parent, false)
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDaggerComponent()
        initViewModel()
    }

    private fun initDaggerComponent() {
        HomeComponent.create(
            (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
            (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider()
        ).also {
            it.inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            title.updateTopMarginOnApplyWindowInsets()
            meetingRoom.setSafeOnClickListener {
                navController.navigate(
                    resId = R.id.to_reservationFragmentDialog,
                    args = null,
                    navOptions = navOptionsFactory.createDefault()
                )
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }
}