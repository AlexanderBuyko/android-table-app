package com.demonstration.table.featurebooking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.demonstration.table.featurebooking.databinding.*

class BookingAdapter(
    private val clickListener: BookingItemViewHolder.ClickListener
) : ListAdapter<BookingListItem, BaseBookingViewHolder<out BookingListItem>>(itemCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBookingViewHolder<out BookingListItem> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.list_item_title -> {
                val binding: ListItemTitleBinding =
                    ListItemTitleBinding.inflate(inflater, parent, false)
                TitleViewHolder(binding)
            }
            R.layout.list_item_subtitle -> {
                val binding: ListItemSubtitleBinding =
                    ListItemSubtitleBinding.inflate(inflater, parent, false)
                SubtitleViewHolder(binding)
            }
            R.layout.list_item_placeholder -> {
                val binding: ListItemPlaceholderBinding =
                    ListItemPlaceholderBinding.inflate(inflater, parent, false)
                PlaceholderViewHolder(binding)
            }
            R.layout.list_item_booking -> {
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
            is BookingItem -> {
                (holder as BookingItemViewHolder).apply {
                    bind(item)
                    setClickListener(clickListener)
                }
            }
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
            is BookingTitle -> R.layout.list_item_title
            is BookingSubtitle -> R.layout.list_item_subtitle
            is BookingPlaceholder -> R.layout.list_item_placeholder
            is BookingItem -> R.layout.list_item_booking
        }
    }

    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<BookingListItem>() {
            override fun areItemsTheSame(
                oldItem: BookingListItem,
                newItem: BookingListItem
            ): Boolean {
                return if (oldItem is BookingItem && newItem is BookingItem) {
                    oldItem.id == newItem.id
                } else {
                    oldItem == newItem
                }
            }

            override fun areContentsTheSame(
                oldItem: BookingListItem,
                newItem: BookingListItem
            ): Boolean = oldItem == newItem
        }
    }
}