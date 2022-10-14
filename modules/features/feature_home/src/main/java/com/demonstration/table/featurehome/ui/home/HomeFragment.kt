package com.demonstration.table.featurehome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.demonstration.baseui.widgets.extentions.navigateWith
import com.demonstration.baseui.widgets.extentions.setSafeOnClickListener
import com.demonstration.baseui.widgets.extentions.updateTopMarginOnApplyWindowInsets
import com.demonstration.baseui.widgets.factories.NavOptionsFactory
import com.demonstration.table.basetable.base.BaseFragment
import com.demonstration.table.basetable.dialogs.CapableOfCompleting
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.coreapi.models.Config
import com.demonstration.table.featurehome.R
import com.demonstration.table.featurehome.dagger.HomeComponent
import com.demonstration.table.featurehome.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            title.updateTopMarginOnApplyWindowInsets()
            meetingRoom.setSafeOnClickListener {
                HomeFragmentDirections.toReservationFragmentDialog()
                    .navigateWith(navController, navOptionsFactory.createDefault())
            }
            openSpace.setSafeOnClickListener {
                val config = Config.Builder()
                    .cancelable(false)
                    .message("Loading")
                    .onConfirmation { navController.navigateUp() }
                    .build()
                navController.navigate(R.id.to_progressFragment, bundleOf("config" to config))
                lifecycleScope.launchWhenResumed {
                    delay(1000)
                    val capableOfCompleting = parentFragmentManager
                        .fragments
                        .filterIsInstance<CapableOfCompleting>()
                        .firstOrNull()
                    capableOfCompleting
                        ?.updateDialogMessage("You have reserved you seats successfully! Thank you for choosing our service ;)")
                }
            }
        }
    }

    private fun initDaggerComponent() {
        HomeComponent.create(
            (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
            (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider()
        ).also {
            it.inject(this)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }
}