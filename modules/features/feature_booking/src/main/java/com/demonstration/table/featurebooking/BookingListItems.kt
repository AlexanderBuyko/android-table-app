package com.demonstration.table.featurebooking

import androidx.annotation.DrawableRes

sealed interface BookingListItem

data class BookingTitle(
    val title: String
) : BookingListItem

data class BookingSubtitle(
    val subtitle: String
) : BookingListItem

object BookingPlaceholder : BookingListItem

data class BookingItem(
    val id: String,
    val roomName: String,
    @DrawableRes
    val imageResource: Int,
    val date: String,
    val time: String,
    val peopleAmount: Int,
    val expanded: Boolean = false
) : BookingListItem