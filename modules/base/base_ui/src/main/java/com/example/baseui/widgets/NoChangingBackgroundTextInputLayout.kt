package com.example.baseui.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.baseui.R
import com.google.android.material.textfield.TextInputLayout

class NoChangingBackgroundTextInputLayout : TextInputLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setError(error: CharSequence?) {
        super.setError(error)
        val defaultBackground = getDefaultBackground()
        defaultBackground?.let { updateBackground(it) }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        val defaultBackground = getDefaultBackground()
        defaultBackground?.let { updateBackground(it) }
    }

    private fun updateBackground(drawable: Drawable) {
        editText?.apply { background = drawable }
    }

    private fun getDefaultBackground(): Drawable? {
        val hasError = !error.isNullOrBlank()
        val backgroundDrawable = if (hasError) {
            R.drawable.input_field_error_background
        } else {
            R.drawable.input_field_background
        }
        return ContextCompat.getDrawable(context, backgroundDrawable)
    }
}