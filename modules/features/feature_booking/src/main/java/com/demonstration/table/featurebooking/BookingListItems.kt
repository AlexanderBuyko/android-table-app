package com.demonstration.table.featurebooking

sealed interface BookingListItem

data class BookingTitle(
    val title: String
) : BookingListItem

data class BookingSubtitle(
    val subtitle: String
) : BookingListItem

object BookingPlaceholder : BookingListItem

data class BookingItem(
    val roomName: String
) : BookingListItem