package com.demonstration.table.featurebooking

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.demonstration.table.featurebooking.databinding.ListItemBookingBinding
import com.demonstration.table.featurebooking.databinding.ListItemPlaceholderBinding
import com.demonstration.table.featurebooking.databinding.ListItemSubtitleBinding
import com.demonstration.table.featurebooking.databinding.ListItemTitleBinding
import com.demonstration.baseui.widgets.extentions.setSafeOnClickListener

abstract class BaseBookingViewHolder<T : BookingListItem>(itemView: ViewBinding) :
    RecyclerView.ViewHolder(itemView.root) {

    abstract fun bind(model: T)
}

class TitleViewHolder(
    private val binding: ListItemTitleBinding
) : BaseBookingViewHolder<BookingTitle>(binding) {

    override fun bind(model: BookingTitle) {
        with(binding) {
            title.text = model.title
        }
    }
}

class SubtitleViewHolder(
    private val binding: ListItemSubtitleBinding
) : BaseBookingViewHolder<BookingSubtitle>(binding) {

    override fun bind(model: BookingSubtitle) {
        with(binding) {
            subtitle.text = model.subtitle
        }
    }
}

class PlaceholderViewHolder(
    binding: ListItemPlaceholderBinding
) : BaseBookingViewHolder<BookingPlaceholder>(binding) {

    override fun bind(model: BookingPlaceholder) {}
}

class BookingItemViewHolder(
    private val binding: ListItemBookingBinding
) : BaseBookingViewHolder<BookingItem>(binding) {

    private var model: BookingItem? = null

    init {
        with(binding) {
            roomPreview.clipToOutline = true
        }
    }

    override fun bind(model: BookingItem) {
        this.model = model
        with(binding) {
            roomName.text = model.roomName
            roomPreview.setImageResource(model.imageResource)
            bookingDate.text = model.date
            bookingTime.text = model.time
            peopleAmount.text =
                itemView.resources.getQuantityString(
                    R.plurals.people_amount,
                    model.peopleAmount,
                    model.peopleAmount
                )
        }
    }

    fun setClickListener(listener: ClickListener) {
        binding.roomPreview.setSafeOnClickListener {
            model?.let {
                listener.invoke(itemView, it, adapterPosition)
            }
        }
    }

    fun isExpanded() = model?.expanded ?: false

    interface ClickListener {
        fun invoke(view: View, item: BookingItem, position: Int)
    }
}
