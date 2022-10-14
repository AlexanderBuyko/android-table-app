package com.demonstration.table.featuregreeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featuregreeting.databinding.FragmentGreetingBinding
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import com.demonstration.table.featuresigninapi.SignInMediator
import com.demonstration.table.basetable.base.BaseFragment
import com.demonstration.baseui.widgets.extentions.setSafeOnClickListener
import com.demonstration.baseui.widgets.extentions.updateTopMarginOnApplyWindowInsets
import javax.inject.Inject

class GreetingFragment : BaseFragment<FragmentGreetingBinding>() {

    @Inject
    lateinit var registrationMediator: RegistrationMediator

    @Inject
    lateinit var signInMediator: SignInMediator

    @Inject
    lateinit var navController: NavController

    private lateinit var viewModel: GreetingViewModel

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentGreetingBinding.inflate(inflater, parent,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDaggerComponent()
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun initDaggerComponent() {
        GreetingComponent
            .create(
                (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
                (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider()
            )
            .inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(GreetingViewModel::class.java)
    }

    private fun setupViews() {
        with(binding) {
            title.updateTopMarginOnApplyWindowInsets()
            registerButton.setSafeOnClickListener {
                registrationMediator.openRegistrationScreen(navController)
            }
            signInButton.setSafeOnClickListener {
                signInMediator.openSignInScreen(navController)
            }
        }
    }
}