package com.demonstration.table.basetable.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.threeten.bp.LocalTime

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker

        val defaultTime = LocalTime.of(9, 0)
        val hour = defaultTime.hour
        val minute = defaultTime.minute

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val result = "%02d:%02d".format(hourOfDay, minute)
        setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to result))
        findNavController().navigateUp()
    }

    companion object {
        const val REQUEST_KEY = "TIME_PICKER_REQUEST_KEY"
        const val BUNDLE_KEY = "TIME_PICKER_BUNDLE_KEY"
    }
}