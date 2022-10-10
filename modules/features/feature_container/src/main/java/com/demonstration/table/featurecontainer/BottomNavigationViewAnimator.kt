package com.demonstration.table.featurecontainer

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationViewAnimator private constructor(
    private val navigationView: BottomNavigationView
) {
    data class Builder(
        private var navigationView: BottomNavigationView? = null
    ) {
        fun navigationView(navigationView: BottomNavigationView) =
            apply { this.navigationView = navigationView }

        fun build(): BottomNavigationViewAnimator {
            checkNotNull(navigationView) { "navigationView wasn't called!" }
            return BottomNavigationViewAnimator(navigationView!!)
        }
    }

    fun animate(animateOpening: Boolean) {
        val endHeight: Int
        val heightAnim: ValueAnimator
        val alphaAnim: ObjectAnimator
        if (animateOpening) {
            navigationView.isVisible = true
            // Need to set any large size, because it will be replaced with the min calculated height
            val heightSpec = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.UNSPECIFIED)
            navigationView.measure(View.MeasureSpec.UNSPECIFIED, heightSpec)
            endHeight = navigationView.measuredHeight

            alphaAnim = ObjectAnimator.ofFloat(navigationView, "alpha", 1f)
        } else {
            endHeight = 0
            alphaAnim = ObjectAnimator.ofFloat(navigationView, "alpha", 0f)
        }
        heightAnim = ValueAnimator.ofInt(navigationView.height, endHeight)
        heightAnim.addUpdateListener { animation ->
            navigationView.layoutParams.height = animation.animatedValue as Int
            navigationView.requestLayout()
        }
        val animSet = AnimatorSet()
        if (animateOpening) {
            animSet.playSequentially(heightAnim, alphaAnim)
        } else {
            animSet.playSequentially(alphaAnim, heightAnim)
        }
        animSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                if (!animateOpening) {
                    navigationView.isVisible = false
                }
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        animSet.start()
    }
}