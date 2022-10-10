package com.demonstration.table.featurecontainer

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.coreapi.providers.activity.ActivityAggregatingProvider
import com.demonstration.table.coreapi.providers.application.models.NavigationId
import com.demonstration.table.corefactory.CelebrityFactory
import com.demonstration.table.featurebookingapi.BookingMediator
import com.demonstration.table.featurecontainer.databinding.ActivityTableBinding
import com.demonstration.table.featurecontainer.databinding.LayoutSplashBinding
import com.demonstration.table.featuregreetingapi.GreetingMediator
import com.demonstration.table.featurehomeapi.HomeMediator
import com.demonstration.table.featuresettingsapi.SettingsMediator
import com.example.baseui.extentions.hasInBackStack
import com.example.baseui.extentions.updateBottomPaddingOnApplyWindowInsets
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.baseui.R as baseR

class TableActivity : AppCompatActivity(), ActivityProvidersHolder {

    @Inject
    lateinit var greetingMediator: GreetingMediator

    @Inject
    lateinit var homeMediator: HomeMediator

    @Inject
    lateinit var bookingMediator: BookingMediator

    @Inject
    lateinit var settingsMediator: SettingsMediator

    @Inject
    lateinit var navigationId: NavigationId

    private lateinit var binding: ActivityTableBinding
    private lateinit var splashBinding: LayoutSplashBinding

    private val navController: NavController
        get() = navHostFragment.navController

    private val navHostFragment: NavHostFragment
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { _, _, arguments ->
            val shouldBeVisible = arguments?.getBoolean("ShowBottomNav", false) == true
            val navigationView = binding.bottomNavigation
            val rootContainer = binding.rootContainer
            val changeVisibility = navigationView.isVisible != shouldBeVisible

            if (changeVisibility) {
                val toHeight: Int
                val heightAnim: ValueAnimator
                val alphaAnim: ObjectAnimator
                if (shouldBeVisible) {
                    navigationView.isVisible = true

                    val widthSpec = MeasureSpec.makeMeasureSpec(rootContainer.width, MeasureSpec.EXACTLY)
                    // Need to set any large size, because it will be replaced with the min calculated height
                    val heightSpec = MeasureSpec.makeMeasureSpec(1000, MeasureSpec.UNSPECIFIED)
                    navigationView.measure(widthSpec, heightSpec)
                    toHeight = navigationView.measuredHeight

                    alphaAnim = ObjectAnimator.ofFloat(navigationView, "alpha", 1f)
                } else {
                    toHeight = 0
                    alphaAnim = ObjectAnimator.ofFloat(navigationView, "alpha", 0f)
                }
                heightAnim = ValueAnimator.ofInt(navigationView.height, toHeight)
                heightAnim.addUpdateListener { animation ->
                    navigationView.layoutParams.height = animation.animatedValue as Int
                    navigationView.requestLayout()
                }
                val animSet = AnimatorSet()
                if (shouldBeVisible) {
                    animSet.playSequentially(heightAnim, alphaAnim)
                } else {
                    animSet.playSequentially(alphaAnim, heightAnim)
                }
                animSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        if (!shouldBeVisible) {
                            navigationView.isVisible = false
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
                animSet.start()
            }
        }

    private val rootContainer by lazy { binding.rootContainer }
    private val bottomNavigation by lazy { binding.bottomNavigation }
    private val curtainLeft by lazy { splashBinding.leftCurtain }
    private val curtainRight by lazy { splashBinding.rightCurtain }
    private val splashIcon by lazy { splashBinding.splashIcon }
    private val bullet by lazy { splashBinding.bulletIcon }

    private var exitAnimationPreparation: Deferred<Unit>? = null

    /*
    * Properties, used to create smooth animation for ime opening.
    * */
    private var startContainerBottom = 0f
    private var endContainerBottom = 0f

    override fun getActivityAggregatingProvider(): ActivityAggregatingProvider {
        return activityAggregatingProvider
            ?: run<ActivityAggregatingProvider> {
                TableActivityComponent.create(
                    (application as AppProvidersHolder).getAggregatingProvider(),
                    CelebrityFactory.createNavControllerProvider(navController)
                ).also { component ->
                    component.inject(this)
                    activityAggregatingProvider = component
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTableBinding.inflate(layoutInflater)
        splashBinding = LayoutSplashBinding.bind(binding.root)
        setContentView(binding.root)
        initDaggerComponent()
        initNavigationGraph()
        initComponents()
        initBottomNavigation()

        handleSplashDuration()
        handleSplashExitAnimation()
        handleSystemInsets()
    }

    override fun onStart() {
        super.onStart()
        navController.addOnDestinationChangedListener(onDestinationChangedListener)
    }

    override fun onStop() {
        super.onStop()
        navController.removeOnDestinationChangedListener(onDestinationChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        activityAggregatingProvider = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            prepareForSplashExitAnimation()
        }
    }

    private fun initDaggerComponent() {
        getActivityAggregatingProvider()
    }

    private fun initNavigationGraph() {
        navController
            .navInflater
            .inflate(navigationId.value)
            .also { navGraph -> navController.graph = navGraph }
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
        bottomNavigation.updateBottomPaddingOnApplyWindowInsets()
        ResourcesCompat.getDrawable(resources, baseR.drawable.ic_table_logo_end, null)
            ?.apply {
                splashIcon.setPadding(
                    resources.getDimension(baseR.dimen.splash_icon_padding).toInt()
                )
            }
            .also { splashIcon.setImageDrawable(it) }
    }

    private fun initBottomNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            if (navController.hasInBackStack(item.itemId)) {
                navController.popBackStack(item.itemId, false)
            } else {
                when (item.itemId) {
                    R.id.homeFragment ->
                        homeMediator.openHomeScreen(navController)
                    R.id.bookingFragment ->
                        bookingMediator.openBookingScreen(navController)
                    R.id.settingsFragment ->
                        settingsMediator.openSettingsScreen(navController)
                }
            }
            true
        }
    }

    private fun handleSystemInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setWindowInsetsAnimationCallback(rootContainer, object :
            WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {

            override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                super.onPrepare(animation)
                startContainerBottom = rootContainer.bottom.toFloat()
            }

            override fun onStart(
                animation: WindowInsetsAnimationCompat,
                bounds: WindowInsetsAnimationCompat.BoundsCompat
            ): WindowInsetsAnimationCompat.BoundsCompat {
                endContainerBottom = rootContainer.bottom.toFloat()
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
                rootContainer.translationY =
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
                    val slideLeft = ObjectAnimator.ofFloat(
                        curtainLeft,
                        View.TRANSLATION_X,
                        0f,
                        -curtainLeft.width.toFloat()
                    )
                    val alphaLeft = ObjectAnimator.ofFloat(curtainLeft, "alpha", 1f, 0f)
                    val slideRight = ObjectAnimator.ofFloat(
                        curtainRight,
                        View.TRANSLATION_X,
                        0f,
                        curtainRight.width.toFloat()
                    )
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
                        ObjectAnimator.ofFloat(
                            bullet,
                            View.TRANSLATION_Y,
                            0f,
                            rootContainer.height.toFloat()
                        ).apply {
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

        private var activityAggregatingProvider: ActivityAggregatingProvider? = null
    }
}