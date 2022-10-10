package com.demostration.table.basetable.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val result = "%02d/%02d/%04d".format(day, month, year)
        setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to result))
        findNavController().navigateUp()
    }

    companion object {
        const val REQUEST_KEY = "DATE_PICKER_REQUEST_KEY"
        const val BUNDLE_KEY = "DATE_PICKER_BUNDLE_KEY"
    }
}