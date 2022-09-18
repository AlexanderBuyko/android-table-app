package com.example.baseui.extentions

import android.widget.EditText

fun EditText.clearFocusIfFocused() {
    if (isFocused) {
        clearFocus()
    }
}