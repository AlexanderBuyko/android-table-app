package com.demonstration.table.featurebooking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.demonstration.table.featurebooking.databinding.ListItemBookingBinding
import com.demonstration.table.featurebooking.databinding.ListItemEmptyListPlaceholderBinding
import com.demonstration.table.featurebooking.databinding.ListItemSubtitleBinding
import com.demonstration.table.featurebooking.databinding.ListItemTitleBinding

class BookingAdapter :
    ListAdapter<BookingListItem, BaseBookingViewHolder<out BookingListItem>>(itemCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBookingViewHolder<out BookingListItem> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            BookingViewType.TITLE.ordinal -> {
                val binding: ListItemTitleBinding =
                    ListItemTitleBinding.inflate(inflater, parent, false)
                TitleViewHolder(binding)
            }
            BookingViewType.SUBTITLE.ordinal -> {
                val binding: ListItemSubtitleBinding =
                    ListItemSubtitleBinding.inflate(inflater, parent, false)
                SubtitleViewHolder(binding)
            }
            BookingViewType.PLACEHOLDER.ordinal -> {
                val binding: ListItemEmptyListPlaceholderBinding =
                    ListItemEmptyListPlaceholderBinding.inflate(inflater, parent, false)
                PlaceholderViewHolder(binding)
            }
            BookingViewType.ITEM.ordinal -> {
                val binding: ListItemBookingBinding =
                    ListItemBookingBinding.inflate(inflater, parent, false)
                BookingItemViewHolder(binding)
            }
            else -> throw IllegalStateException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: BaseBookingViewHolder<out BookingListItem>,
        position: Int
    ) {
        when (val item = getItem(position)) {
            is BookingItem ->
                (holder as BookingItemViewHolder).bind(item)
            is BookingPlaceholder ->
                (holder as PlaceholderViewHolder).bind(item)
            is BookingSubtitle ->
                (holder as SubtitleViewHolder).bind(item)
            is BookingTitle ->
                (holder as TitleViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BookingTitle -> BookingViewType.TITLE.ordinal
            is BookingSubtitle -> BookingViewType.SUBTITLE.ordinal
            is BookingPlaceholder -> BookingViewType.PLACEHOLDER.ordinal
            is BookingItem -> BookingViewType.ITEM.ordinal
        }
    }

    enum class BookingViewType {
        TITLE, SUBTITLE, PLACEHOLDER, ITEM
    }

    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<BookingListItem>() {
            override fun areItemsTheSame(
                oldItem: BookingListItem,
                newItem: BookingListItem
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: BookingListItem,
                newItem: BookingListItem
            ): Boolean = oldItem == newItem
        }
    }
}