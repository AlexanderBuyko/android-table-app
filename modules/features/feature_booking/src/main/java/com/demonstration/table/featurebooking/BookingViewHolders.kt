package com.demonstration.table.featurebooking

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.demonstration.table.featurebooking.databinding.ListItemBookingBinding
import com.demonstration.table.featurebooking.databinding.ListItemEmptyListPlaceholderBinding
import com.demonstration.table.featurebooking.databinding.ListItemSubtitleBinding
import com.demonstration.table.featurebooking.databinding.ListItemTitleBinding

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
    binding: ListItemEmptyListPlaceholderBinding
) : BaseBookingViewHolder<BookingPlaceholder>(binding) {

    override fun bind(model: BookingPlaceholder) {}
}

class BookingItemViewHolder(
    private val binding: ListItemBookingBinding
) : BaseBookingViewHolder<BookingItem>(binding) {

    init {
        binding.roomPreview.clipToOutline = true
    }

    override fun bind(model: BookingItem) {
        with(binding) {
            roomName.text = model.roomName
        }
    }
}
