package com.example.baseui.extentions

import android.content.res.Resources
import kotlin.math.roundToInt

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()