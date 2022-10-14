package com.demonstration.table.featurehome.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featurehome.R
import com.demonstration.table.featurehome.dagger.HomeComponent
import com.demonstration.table.featurehome.databinding.FragmentReservationBinding
import com.demonstration.table.basetable.base.BaseBottomSheetDialogFragment
import com.demonstration.table.basetable.dialogs.DatePickerFragment
import com.demonstration.table.basetable.dialogs.TimePickerFragment
import com.demonstration.table.basetable.utils.DateUtils
import com.demonstration.baseui.widgets.extentions.navigateWith
import com.demonstration.baseui.widgets.extentions.setSafeOnClickListener
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import javax.inject.Inject


class ReservationDialogFragment :
    BaseBottomSheetDialogFragment<FragmentReservationBinding>() {

    @Inject
    lateinit var navController: NavController

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentReservationBinding.inflate(inflater, parent, false)
    }

    private var dateRequested = false
    private var startTimeRequested = false
    private var endTimeRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        HomeComponent.create(
            appAggregatingProvider = (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
            activityAggregatingProvider = (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider()
        ).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleResultListeners()
        with(binding) {
            roomPreview.clipToOutline = true
            roomName.text = "Meeting room"
            dateField.text = getDefaultDateField()
            dateField.setSafeOnClickListener {
                dateRequested = true
                ReservationDialogFragmentDirections.toDatePickerFragment()
                    .navigateWith(navController)
            }
            startTimeField.text = getDefaultStartTimeField()
            startTimeField.setSafeOnClickListener {
                startTimeRequested = true
                ReservationDialogFragmentDirections
                    .toTimePickerFragment()
                    .navigateWith(navController)
            }
            endTimeField.text = getDefaultEndTimeField()
            endTimeField.setSafeOnClickListener {
                endTimeRequested = true
                ReservationDialogFragmentDirections
                    .toTimePickerFragment()
                    .navigateWith(navController)
            }
            availablePlacesCount.text = resources.getQuantityString(
                R.plurals.places_count, 9, 9)
        }
    }

    private fun handleResultListeners() {
        setFragmentResultListener(DatePickerFragment.REQUEST_KEY) { _, bundle ->
            when {
                dateRequested -> {
                    binding.dateField.text = bundle.getString(DatePickerFragment.BUNDLE_KEY)
                    dateRequested = false
                }
            }
        }
        setFragmentResultListener(TimePickerFragment.REQUEST_KEY) { _, bundle ->
            when {
                startTimeRequested -> {
                    binding.startTimeField.text = bundle.getString(TimePickerFragment.BUNDLE_KEY)
                    startTimeRequested = false
                }
                endTimeRequested -> {
                    binding.endTimeField.text = bundle.getString(TimePickerFragment.BUNDLE_KEY)
                    endTimeRequested = false
                }
            }
        }
    }

    private fun getDefaultDateField(): String {
        val tomorrowDate = LocalDate.now().plusDays(1)
        return DateUtils.convertToFullDate(tomorrowDate)
    }

    private fun getDefaultStartTimeField(): String {
        val startTime = LocalTime.of(8, 0)
        return DateUtils.convertToShortTime(startTime)
    }

    private fun getDefaultEndTimeField(): String {
        val startTime = LocalTime.of(9, 0)
        return DateUtils.convertToShortTime(startTime)
    }
}