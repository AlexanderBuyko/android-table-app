package com.demonstration.table.featurehome.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.demonstration.baseui.widgets.extentions.SpannablePart
import com.demonstration.baseui.widgets.extentions.addSpannableParts
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
import com.demonstration.baseui.widgets.widgets.WorkspaceSelectionView
import com.demonstration.baseui.widgets.widgets.models.AvailableWorkspace
import com.demonstration.baseui.widgets.widgets.models.Position
import com.demonstration.baseui.widgets.widgets.models.ReservedWorkspace
import com.demonstration.baseui.widgets.widgets.models.Workspace
import com.demonstration.table.basetable.dialogs.CapableOfCompleting
import com.demonstration.table.coreapi.models.Config
import kotlinx.coroutines.delay
import com.example.baseui.R as baseR
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
            roomName.text = "Open space"
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
                R.plurals.places_left_count, 5, 5
            )

            val workspacesList = generateWorkspacesList()
            workspacesPicker.setWorkspaces(workspacesList)
            workspacesPicker.setCallback(object : WorkspaceSelectionView.Callback {
                override fun onSeatSelected(workspace: Workspace) {
                    selectedSeat.isVisible = true
                    selectedSeat.addSpannableParts(
                        firstSpannablePart(),
                        secondSpannablePart(workspace.name)
                    )
                }

                override fun onSeatReleased() {
                    selectedSeat.isVisible = false
                    selectedSeat.text = ""
                }
            })
            bookButton.setSafeOnClickListener {
                val config = Config.Builder()
                    .cancelable(false)
                    .onConfirmation {
                        lifecycleScope.launchWhenResumed {
                            navController.navigateUp()
                            delay(500)
                            setFragmentResult(REQUEST_KEY, bundleOf(HAVE_RESERVATION_PASSED to true))
                            navController.navigateUp()
                        }
                    }
                    .build()
                navController.navigate(R.id.to_progressFragment, bundleOf("config" to config))
                lifecycleScope.launchWhenStarted {
                    delay(2500)
                    val capableOfCompleting = parentFragmentManager
                        .fragments
                        .filterIsInstance<CapableOfCompleting>()
                        .firstOrNull()
                    capableOfCompleting?.updateDialogMessage("You have reserved you seats successfully! Thank you for choosing our service ;)")
                }
            }
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

    private fun generateWorkspacesList(): List<Workspace> {
        return listOf(
            ReservedWorkspace(name = "S1", positions = arrayOf(Position(0, 0), Position(0, 1))),
            AvailableWorkspace(name = "S2", positions = arrayOf(Position(1, 0))),
            AvailableWorkspace(name = "S3", positions = arrayOf(Position(3, 0))),
            ReservedWorkspace(name = "S4", positions = arrayOf(Position(4, 0))),
            AvailableWorkspace(name = "S5", positions = arrayOf(Position(1, 1))),
            ReservedWorkspace(name = "S6", positions = arrayOf(Position(3, 1), Position(4, 1))),
            ReservedWorkspace(name = "S7", positions = arrayOf(Position(0, 3))),
            AvailableWorkspace(name = "S8", positions = arrayOf(Position(1, 3), Position(2, 3))),
            ReservedWorkspace(name = "S9", positions = arrayOf(Position(3, 3))),
            ReservedWorkspace(name = "S10", positions = arrayOf(Position(4, 3), Position(4, 4))),
            ReservedWorkspace(name = "S11", positions = arrayOf(Position(0, 4))),
            AvailableWorkspace(name = "S12", positions = arrayOf(Position(1, 4))),
            ReservedWorkspace(name = "S13", positions = arrayOf(Position(2, 4))),
            ReservedWorkspace(name = "S14", positions = arrayOf(Position(3, 4)))
        )
    }

    private fun firstSpannablePart(): SpannablePart {
        return SpannablePart(
            getString(R.string.reservation_selected_title),
            baseR.style.Theme_Table_TextAppearance_UI14R
        )
    }

    private fun secondSpannablePart(name: String): SpannablePart {
        return SpannablePart(name, baseR.style.Theme_Table_TextAppearance_UI14SB)
    }

    companion object {
        const val REQUEST_KEY = "RESERVATION_FRAGMENT_REQUEST_KEY"
        const val HAVE_RESERVATION_PASSED = "HAVE_RESERVATION_PASSED"
    }
}