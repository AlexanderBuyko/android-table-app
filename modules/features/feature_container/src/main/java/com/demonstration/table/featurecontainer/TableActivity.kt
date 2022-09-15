package com.demonstration.table.featurecontainer

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import com.demonstration.table.coreapi.holders.ProvidersHolder
import com.demonstration.table.featurecomponentsapi.ComponentsMediator
import com.demonstration.table.featurecontainer.databinding.ActivityTableBinding
import com.demonstration.table.featurecontainer.databinding.LayoutSplashBinding
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.baseui.R as baseR

class TableActivity : AppCompatActivity() {

    @Inject
    lateinit var containerMediator: ComponentsMediator

    @Inject
    lateinit var registrationMediator: RegistrationMediator

    private lateinit var binding: ActivityTableBinding
    private lateinit var splashBinding: LayoutSplashBinding

    private val componentContainer by lazy { binding.vFragmentContainer }
    private val curtainLeft by lazy { splashBinding.vIvCurtainLeft }
    private val curtainRight by lazy { splashBinding.vIvCurtainRight }
    private val splashIcon by lazy { splashBinding.vIvSplashIcon }
    private val bullet by lazy { splashBinding.vIvBullet }

    private var exitAnimationPreparation: Deferred<Unit>? = null

    /*
    * Properties, used to create smooth animation for ime opening.
    * */
    private var startContainerBottom = 0f
    private var endContainerBottom = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTableBinding.inflate(layoutInflater)
        splashBinding = LayoutSplashBinding.bind(binding.root)
        setContentView(binding.root)
        initDaggerComponent()
        initComponents()

        startFragment()

        handleSplashDuration()
        handleSplashExitAnimation()
        handleSystemInsets()
    }

    private fun startFragment() {
        registrationMediator.openRegistrationScreen(
            supportFragmentManager, R.id.vFragmentContainer
        )
    }

    private fun initDaggerComponent() {
        TableActivityComponent
            .create((application as ProvidersHolder).getAggregatingProvider())
            .inject(this)
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
        ResourcesCompat.getDrawable(resources, baseR.drawable.ic_table_logo_end, null)
            ?.apply { splashIcon.setPadding(resources.getDimension(baseR.dimen.splash_icon_padding).toInt()) }
            .also { splashIcon.setImageDrawable(it) }
    }

    private fun handleSystemInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(componentContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top, bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        ViewCompat.setWindowInsetsAnimationCallback(componentContainer, object :
            WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {

            override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                super.onPrepare(animation)
                startContainerBottom = componentContainer.bottom.toFloat()
            }

            override fun onStart(
                animation: WindowInsetsAnimationCompat,
                bounds: WindowInsetsAnimationCompat.BoundsCompat
            ): WindowInsetsAnimationCompat.BoundsCompat {
                endContainerBottom = componentContainer.bottom.toFloat()
                return bounds
            }

            override fun onProgress(
                insets: WindowInsetsCompat,
                runningAnimations: MutableList<WindowInsetsAnimationCompat>
            ): WindowInsetsCompat {
                val imeAnimation = runningAnimations.find {
                    it.typeMask and WindowInsetsCompat.Type.ime() != 0
                } ?: return insets

                // Offset the view based on the interpolated fraction of the IME animation.
                componentContainer.translationY =
                    (startContainerBottom - endContainerBottom) * (1 - imeAnimation.interpolatedFraction)

                return insets
            }
        })
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