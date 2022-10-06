package com.example.baseui.extentions

import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

/**
 * @author [Chris Banes](https://chris.banes.dev/2019/04/12/insets-listeners-to-layouts/)
 */
fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, Initials) -> Unit) {
    // Create a snapshot of the view's padding state
    val initials = recordInitials(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initials)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}


fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.updateTopMarginOnApplyWindowInsets() {
    doOnApplyWindowInsets { view, windowInsets, initials ->
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin =
                initials.margins.top + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top
        }
    }
}

fun View.updateBottomMarginOnApplyWindowInsets() {
    doOnApplyWindowInsets { view, windowInsets, initials ->
        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin =
                initials.margins.bottom + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
        }
    }
}

fun View.updateTopPaddingOnApplyWindowInsets() {
    doOnApplyWindowInsets { view, windowInsets, initials ->
        view.updatePadding(
            top = initials.padding.top + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top
        )
    }
}

fun View.updateBottomPaddingOnApplyWindowInsets() {
    doOnApplyWindowInsets { view, windowInsets, initials ->
        view.updatePadding(
            bottom = initials.padding.bottom + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
        )
    }
}

private fun recordInitials(view: View): Initials {
    val initialPadding =
        recordInitialPaddingForView(view)
    val initialMargins =
        recordInitialMarginsForView(view)
    val initialSize =
        recordInitialSizeForView(view)
    return Initials(
        initialPadding,
        initialMargins,
        initialSize
    )
}

private fun recordInitialPaddingForView(view: View) =
    InitialPadding(
        view.paddingLeft,
        view.paddingTop,
        view.paddingRight,
        view.paddingBottom
    )

private fun recordInitialMarginsForView(view: View): InitialMargins =
    (view.layoutParams as? ViewGroup.MarginLayoutParams)?.run {
        InitialMargins(
            leftMargin,
            topMargin,
            rightMargin,
            bottomMargin
        )
    } ?: InitialMargins(0, 0, 0, 0)

private fun recordInitialSizeForView(view: View) = with(view) {
    if (view.layoutParams != null) {
        val width = if (layoutParams.width >= 0) layoutParams.width else width
        val height = if (layoutParams.height >= 0) layoutParams.height else height
        InitialSize(width, height)
    } else {
        InitialSize(width, height)
    }
}

data class Initials(
    val padding: InitialPadding,
    val margins: InitialMargins,
    val size: InitialSize
)

data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)
data class InitialSize(val width: Int, val height: Int)
data class InitialMargins(val left: Int, val top: Int, val right: Int, val bottom: Int)