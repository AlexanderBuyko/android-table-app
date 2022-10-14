package com.demonstration.table.featurebooking

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.baseui.R as baseR

class BookingItemDecorator : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter as? BookingAdapter ?: return
        val position = parent.getChildAdapterPosition(view)
        if (position !in 0 until adapter.itemCount) {
            return
        }
        val resources = view.resources
        val largeMedium = resources.getDimension(baseR.dimen.margin_large).toInt()
        val tinyMedium = resources.getDimension(baseR.dimen.margin_tiny).toInt()
        val teenyMedium = resources.getDimension(baseR.dimen.margin_teeny).toInt()
        val mediumMedium = resources.getDimension(baseR.dimen.margin_medium).toInt()

        when (adapter.getItemViewType(position)) {
            R.layout.list_item_title ->
                outRect.set(largeMedium, largeMedium, largeMedium, 0)
            R.layout.list_item_subtitle ->
                outRect.set(largeMedium, tinyMedium, largeMedium, teenyMedium)
            R.layout.list_item_booking -> {
                outRect.set(largeMedium, mediumMedium, largeMedium, 0)
            }
        }
    }
}

