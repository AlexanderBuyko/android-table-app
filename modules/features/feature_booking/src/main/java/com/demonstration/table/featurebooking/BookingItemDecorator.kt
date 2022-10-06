package com.demonstration.table.featurebooking

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.baseui.extentions.dp
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
            BookingAdapter.BookingViewType.TITLE.ordinal ->
                outRect.set(largeMedium, largeMedium, largeMedium, 0)
            BookingAdapter.BookingViewType.SUBTITLE.ordinal ->
                outRect.set(largeMedium, tinyMedium, largeMedium, teenyMedium)
            BookingAdapter.BookingViewType.ITEM.ordinal -> {
                outRect.set(largeMedium, mediumMedium, largeMedium, 0)
            }
        }
    }
}

