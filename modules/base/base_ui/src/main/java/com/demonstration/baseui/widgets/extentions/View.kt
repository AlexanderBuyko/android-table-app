package com.demonstration.baseui.widgets.extentions

import android.os.SystemClock
import android.view.View
import com.demonstration.baseui.widgets.extentions.SafeClickListener.Companion.BUTTON_CLICK_TIME_GAP

class SafeClickListener(
    private val timeout: Long,
    private val onSafeClick: (View) -> Unit
) : View.OnClickListener {

    companion object {
        const val BUTTON_CLICK_TIME_GAP = 500L
    }

    private var lastTimeClicked = 0L

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked > timeout) {
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeClick(v)
        }
    }
}

fun View.setSafeOnClickListener(
    timeout: Long = BUTTON_CLICK_TIME_GAP,
    onSafeClick: (View) -> Unit
) {
    val safeClickListener = SafeClickListener(timeout) {
        onSafeClick(it)
    }

    setOnClickListener(safeClickListener)
}

fun View.setSafeOnClickListener(
    onSafeClick: (View) -> Unit
) {
    setSafeOnClickListener(BUTTON_CLICK_TIME_GAP, onSafeClick)
}