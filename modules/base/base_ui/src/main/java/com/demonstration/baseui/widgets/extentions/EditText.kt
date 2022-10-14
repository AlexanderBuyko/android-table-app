package com.demonstration.baseui.widgets.extentions

import android.widget.EditText

fun EditText.clearFocusIfFocused() {
    if (isFocused) {
        clearFocus()
    }
}