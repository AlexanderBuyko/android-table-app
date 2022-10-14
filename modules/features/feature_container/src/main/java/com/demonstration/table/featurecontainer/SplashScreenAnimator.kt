package com.demonstration.table.featurecontainer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.window.SplashScreenView
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.demonstration.table.featurecontainer.databinding.LayoutSplashBinding

class SplashScreenAnimator private constructor(
    private val splashBinding: LayoutSplashBinding
) {

    private val curtainLeft by lazy { splashBinding.leftCurtain }
    private val curtainRight by lazy { splashBinding.rightCurtain }
    private val splashIcon by lazy { splashBinding.splashIcon }
    private val bullet by lazy { splashBinding.bulletIcon }

    data class Builder(
        var splashBinding: LayoutSplashBinding? = null,
    ) {
        fun splashBinding(splashBinding: LayoutSplashBinding) =
            apply { this.splashBinding = splashBinding }

        fun build(): SplashScreenAnimator {
            checkNotNull(splashBinding) { "splashBinding wasn't called!" }
            return SplashScreenAnimator(splashBinding!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun animate(splashScreenView: SplashScreenView, containerHeight: Float) {
        val slideLeft = ObjectAnimator.ofFloat(curtainLeft, View.TRANSLATION_X, 0f, -curtainLeft.width.toFloat())
        val alphaLeft = ObjectAnimator.ofFloat(curtainLeft, "alpha", 1f, 0f)
        val slideRight = ObjectAnimator.ofFloat(curtainRight, View.TRANSLATION_X, 0f, curtainRight.width.toFloat())
        val alphaRight = ObjectAnimator.ofFloat(curtainRight, "alpha", 1f, 0f)

        val curtainsPullingAnim = AnimatorSet().apply {
            playTogether(slideLeft, alphaLeft, slideRight, alphaRight)
            interpolator = DecelerateInterpolator()
            duration = CURTAINS_PULLING_ANIMATION_DURATION
            startDelay = BULLET_FLYING_ANIMATION_DURATION / 2

            doOnStart { splashIcon.visibility = View.GONE }
            doOnEnd {
                curtainLeft.visibility = View.GONE
                curtainRight.visibility = View.GONE
            }
        }

        val bulletFlyingAnim =
            ObjectAnimator.ofFloat(bullet, View.TRANSLATION_Y, 0f, containerHeight).apply {
                interpolator = DecelerateInterpolator()
                duration = BULLET_FLYING_ANIMATION_DURATION

                doOnEnd { bullet.visibility = View.GONE }
            }

        AnimatorSet().apply {
            playTogether(bulletFlyingAnim, curtainsPullingAnim)
            doOnStart { splashScreenView.remove() }
            start()
        }
    }

    companion object {
        private const val CURTAINS_PULLING_ANIMATION_DURATION = 1000L
        private const val BULLET_FLYING_ANIMATION_DURATION = 500L
    }
}