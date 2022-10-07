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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.bookingItems) {
            updateTopPaddingOnApplyWindowInsets()
            scrollToPosition(0)

            adapter = bookingsAdapter
            itemAnimator = BookingItemAnimator()
            addItemDecoration(BookingItemDecorator())

            val listItems = generateTestList()
            bookingsAdapter.submitList(listItems)
        }
    }

    private fun initDaggerComponent() {
        BookingComponent.create(
            (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
            (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider(),
        ).also {
            it.inject(this)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(BookingViewModel::class.java)
    }

    private fun initAdapter() {
        bookingsAdapter = BookingAdapter(bookingItemClickListener)
    }

    private fun generateTestList() = listOf(
        BookingTitle(getString(R.string.booking_title)),
        BookingSubtitle(getString(R.string.booking_subtitle)),
        BookingItem(
            id = "1",
            roomName = "Meeting room",
            imageResource = R.drawable.ic_meeting_room,
            date = "October 12, 2022",
            time = "09:00 - 14:00",
            peopleAmount = 1
        ),
        BookingItem(
            id = "2",
            roomName = "Break out",
            imageResource = R.drawable.ic_break_out,
            date = "October 12, 2022",
            time = "09:00 - 14:00",
            peopleAmount = 2
        ),
        BookingItem(
            id = "3",
            roomName = "Lounge",
            imageResource = R.drawable.ic_lounge,
            date = "October 12, 2022",
            time = "09:00 - 14:00",
            peopleAmount = 3
        )
        /*BookingPlaceholder*/
    )

    private val bookingItemClickListener = object : BookingItemViewHolder.ClickListener {
        override fun invoke(view: View, item: BookingItem, position: Int) {
            bookingsAdapter.run {
                currentList
                    .map {
                        if (it !is BookingItem || it.id != item.id) return@map it
                        return@map it.copy(expanded = !it.expanded)
                    }
                    .also(this::submitList)
            }
        }
    }
}