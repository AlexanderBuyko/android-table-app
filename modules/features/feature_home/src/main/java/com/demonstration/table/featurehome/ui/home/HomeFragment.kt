package com.demonstration.table.featurehome.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.demonstration.baseui.widgets.extentions.navigateWith
import com.demonstration.baseui.widgets.extentions.setSafeOnClickListener
import com.demonstration.baseui.widgets.extentions.updateTopMarginOnApplyWindowInsets
import com.demonstration.baseui.widgets.factories.NavOptionsFactory
import com.demonstration.table.basetable.base.BaseFragment
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featurebookingapi.BookingMediator
import com.demonstration.table.featurehome.dagger.HomeComponent
import com.demonstration.table.featurehome.databinding.FragmentHomeBinding
import com.demonstration.table.featurehome.ui.reservation.ReservationDialogFragment
import com.demonstration.table.featurehome.ui.reservation.ReservationDialogFragment.Companion.HAVE_RESERVATION_PASSED
import kotlinx.coroutines.delay
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var navController: NavController

    @Inject
    lateinit var navOptionsFactory: NavOptionsFactory

    @Inject
    lateinit var bookingMediator: BookingMediator

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
        handleFragmentResults()
        with(binding) {
            title.updateTopMarginOnApplyWindowInsets()
            openSpace.setSafeOnClickListener {
                HomeFragmentDirections.toReservationFragmentDialog()
                    .navigateWith(navController, navOptionsFactory.createDefault())
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

    private fun handleFragmentResults() {
        setFragmentResultListener(ReservationDialogFragment.REQUEST_KEY) { _, bundle ->
            if (bundle.getBoolean(HAVE_RESERVATION_PASSED)) {
                lifecycleScope.launchWhenResumed {
                    delay(500)
                    bookingMediator.openBookingScreenWithSlideAnim(navController)
                }
            }
        }
    }
}