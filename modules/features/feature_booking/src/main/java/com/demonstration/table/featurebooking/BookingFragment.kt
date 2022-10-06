package com.demonstration.table.featurebooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featurebooking.databinding.FragmentBookingBinding
import com.demostration.table.basetable.base.BaseFragment
import com.example.baseui.extentions.updateTopPaddingOnApplyWindowInsets

class BookingFragment : BaseFragment<FragmentBookingBinding>() {

    private lateinit var viewModel: BookingViewModel

    private lateinit var bookingsAdapter: BookingAdapter

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentBookingBinding.inflate(inflater, parent, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDaggerComponent()
        initViewModel()
        initAdapter()
    }

    private fun initDaggerComponent() {
        BookingComponent.create(
            (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
            (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider(),
        ).also {
            it.inject(this)
        }
    }

    private fun initAdapter() {
        bookingsAdapter = BookingAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.bookingItems) {
            updateTopPaddingOnApplyWindowInsets()
            adapter = bookingsAdapter
            addItemDecoration(BookingItemDecorator())

            val listItems = listOf(
                BookingTitle(getString(R.string.booking_title)),
                BookingSubtitle(getString(R.string.booking_subtitle)),
                BookingItem("Meeting room"),
                BookingItem("Break out"),
                BookingItem("Meeting room"),
                BookingItem("Break out"),
                BookingItem("Meeting room"),
                BookingItem("Break out"),
                BookingItem("Meeting room"),
                BookingItem("Break out"),
                /*BookingPlaceholder*/
            )
            bookingsAdapter.submitList(listItems)
            scrollToPosition(0)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
    }
}