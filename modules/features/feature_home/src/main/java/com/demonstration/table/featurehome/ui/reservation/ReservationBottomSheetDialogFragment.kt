package com.demonstration.table.featurehome.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demonstration.table.featurehome.databinding.FragmentReservationBinding
import com.demostration.table.basetable.base.BaseBottomSheetDialogFragment


class ReservationBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment<FragmentReservationBinding>() {

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentReservationBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            roomName.text = "Meeting room"
            roomPreview.clipToOutline = true
        }
    }
}