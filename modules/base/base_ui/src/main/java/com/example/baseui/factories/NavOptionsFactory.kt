package com.example.baseui.factories

import androidx.navigation.NavOptions
import com.example.baseui.R
import javax.inject.Inject

class NavOptionsFactory @Inject constructor() {

    /*
    * Creates default NavOptions instance
    * */
    fun create() = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
}