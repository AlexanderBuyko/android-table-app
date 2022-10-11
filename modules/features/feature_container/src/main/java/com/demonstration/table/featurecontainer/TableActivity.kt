package com.demonstration.table.featurecontainer

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.DialogFragmentNavigator
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
        NavController.OnDestinationChangedListener { _, navDestination, arguments ->
            val expectedVisibility = arguments?.getBoolean("ShowBottomNav", false) == true ||
                    navDestination is DialogFragmentNavigator.Destination
            val navigationView = binding.bottomNavigation
            val changeVisibility = navigationView.isVisible != expectedVisibility

            if (changeVisibility) {
                BottomNavigationViewAnimator.Builder()
                    .navigationView(navigationView)
                    .build()
                    .apply { animate(expectedVisibility) }
            }
        }

    private val rootContainer by lazy { binding.rootContainer }
    private val bottomNavigation by lazy { binding.bottomNavigation }

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
            with(splashBinding) {
                val bitmap = splashIcon.drawToBitmap()
                val leftBitmapPart = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width / 2, bitmap.height)
                val rightBitmapPart =
                    Bitmap.createBitmap(bitmap, bitmap.width / 2, 0, bitmap.width / 2, bitmap.height)
                leftCurtain.setImageBitmap(leftBitmapPart)
                rightCurtain.setImageBitmap(rightBitmapPart)
            }
        }
    }

    private fun initComponents() {
        bottomNavigation.updateBottomPaddingOnApplyWindowInsets()
        ResourcesCompat.getDrawable(resources, baseR.drawable.ic_table_logo_end, null)
            ?.apply {
                splashBinding.splashIcon.setPadding(
                    resources.getDimension(baseR.dimen.splash_icon_padding).toInt()
                )
            }
            .also { splashBinding.splashIcon.setImageDrawable(it) }
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
        val animator = SplashScreenAnimator.Builder()
            .splashBinding(splashBinding)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                lifecycleScope.launch {
                    exitAnimationPreparation?.await()
                    animator.animate(
                        splashScreenView = splashScreenView,
                        containerHeight = rootContainer.height.toFloat()
                    )
                }
            }
        } else {
            with(splashBinding) {
                leftCurtain.isVisible = false
                rightCurtain.isVisible = false
                splashIcon.isVisible = false
                bulletIcon.isVisible = false
            }
        }
    }

    companion object {
        private const val MINIMUM_SPLASH_ANIMATION_DURATION = 1200

        private var activityAggregatingProvider: ActivityAggregatingProvider? = null
    }
}