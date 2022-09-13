package com.demonstation.table

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import com.demonstration.table.R
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var curtainLeft: ImageView
    private lateinit var curtainRight: ImageView
    private lateinit var componentContainer: LinearLayout
    private lateinit var splashIcon: ImageView
    private lateinit var bullet: ImageView

    private var exitAnimationPreparation: Deferred<Unit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()

        handleSplashDuration()
        handleSplashExitAnimation()
        handleSystemInsets()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            prepareForSplashExitAnimation()
        }
    }

    private fun prepareForSplashExitAnimation() {
        exitAnimationPreparation = lifecycleScope.async {
            val bitmap = splashIcon.drawToBitmap()
            val leftBitmapPart = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width / 2, bitmap.height)
            val rightBitmapPart =
                Bitmap.createBitmap(bitmap, bitmap.width / 2, 0, bitmap.width / 2, bitmap.height)
            curtainLeft.setImageBitmap(leftBitmapPart)
            curtainRight.setImageBitmap(rightBitmapPart)
        }
    }

    private fun initComponents() {
        curtainLeft = findViewById(R.id.vIvCurtainLeft)
        curtainRight = findViewById(R.id.vIvCurtainRight)
        componentContainer = findViewById(R.id.lLlComponentContainer)
        splashIcon = findViewById<ImageView>(R.id.vIvSplashIcon).apply {
            ResourcesCompat.getDrawable(resources, R.drawable.ic_table_logo_end, null)
                ?.apply { setPadding(resources.getDimension(R.dimen.splash_icon_padding).toInt()) }
                .also { setImageDrawable(it) }
        }
        bullet = findViewById(R.id.vIvBullet)
    }

    private fun handleSystemInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(componentContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top, bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun handleSplashDuration() {
        val currentTimeMillis = System.currentTimeMillis()
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (currentTimeMillis + MINIMUM_SPLASH_ANIMATION_DURATION < System.currentTimeMillis()) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    private fun handleSplashExitAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                lifecycleScope.launch {
                    exitAnimationPreparation?.await()
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
                        ObjectAnimator.ofFloat(bullet, View.TRANSLATION_Y, 0f, componentContainer.height.toFloat()).apply {
                            interpolator = DecelerateInterpolator()
                            duration = BULLET_FLYING_ANIMATION_DURATION

                            doOnEnd { bullet.visibility = View.VISIBLE }
                        }

                    AnimatorSet().apply {
                        playTogether(bulletFlyingAnim, curtainsPullingAnim)
                        doOnStart { splashScreenView.remove() }
                        start()
                    }
                }
            }
        }
    }

    companion object {
        private const val CURTAINS_PULLING_ANIMATION_DURATION = 1000L
        private const val BULLET_FLYING_ANIMATION_DURATION = 500L
        private const val MINIMUM_SPLASH_ANIMATION_DURATION = 1200
    }
}