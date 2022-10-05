package com.example.baseui.extentions

import androidx.annotation.IdRes
import androidx.navigation.NavController

/**
 * Check whether we have destination in back stack or not
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun NavController.hasInBackStack(@IdRes destinationId: Int) = kotlin.runCatching {
    getBackStackEntry(destinationId)
}.isSuccess