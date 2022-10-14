package com.demonstration.baseui.widgets.extentions

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleRes


data class SpannablePart(
    val string: String,
    @StyleRes val textAppearance: Int,
    val listener: ((View) -> Unit)? = null
)

fun TextView.addSpannableParts(vararg parts: SpannablePart) {
    val resultSignature = parts
        .map { part -> part.string }
        .reduce { result, part -> result.plus("  $part") }

    var partStartIndex: Int
    val spannableString = SpannableString(resultSignature)
    for (part: SpannablePart in parts) {
        partStartIndex = spannableString.indexOf(part.string)
        setTextAppearanceSpan(part, spannableString, partStartIndex)
        setClickableSpan(part, spannableString, partStartIndex)
    }
    highlightColor = Color.TRANSPARENT
    movementMethod = LinkMovementMethod.getInstance()
    setText(spannableString, TextView.BufferType.SPANNABLE)
}

private fun TextView.setTextAppearanceSpan(
    part: SpannablePart,
    spannableString: SpannableString,
    partStartIndex: Int
) {
    val textAppearanceSpan = TextAppearanceSpan(context, part.textAppearance)
    spannableString.setSpan(
        textAppearanceSpan,
        partStartIndex,
        partStartIndex + part.string.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

private fun setClickableSpan(
    part: SpannablePart,
    spannableString: SpannableString,
    partStartIndex: Int
) {
    part.listener?.let {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.isUnderlineText = false
            }

            override fun onClick(view: View) {
                it.invoke(view)
            }
        }
        spannableString.setSpan(
            clickableSpan,
            partStartIndex,
            partStartIndex + part.string.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}