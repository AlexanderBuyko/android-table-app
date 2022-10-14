package com.demonstration.baseui.widgets.factories

import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import com.example.baseui.R
import javax.inject.Inject

class NavOptionsFactory @Inject constructor() {

    /*
    * Creates default NavOptions instance
    * */
    fun createDefault(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }

    /*
    * Creates NavOptions instance with the destination to pop up to
    * */
    fun create(@IdRes destinationId:Int): NavOptions {
        return NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(destinationId, true)
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }
}