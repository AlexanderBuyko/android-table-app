package com.demonstration.table.featuresignin.recovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featuregreetingapi.GreetingMediator
import com.demonstration.table.featuresignin.SignInComponent
import com.demonstration.table.featuresignin.databinding.FragmentRecoveryBinding
import com.demostration.table.basetable.base.BaseFragment
import com.example.baseui.extentions.setSafeOnClickListener
import com.example.baseui.extentions.updateTopMarginOnApplyWindowInsets
import javax.inject.Inject


class RecoveryFragment : BaseFragment<FragmentRecoveryBinding>() {

    @Inject
    lateinit var greetingMediator: GreetingMediator

    @Inject
    lateinit var navController: NavController

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentRecoveryBinding.inflate(inflater, parent,false)
    }

    private lateinit var viewModel: RecoveryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initDaggerComponent()
    }

    private fun initDaggerComponent() {
        SignInComponent
            .create(
                (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
                (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider()
            )
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RecoveryViewModel::class.java)
    }

    private fun setupViews() {
        with(binding) {
            title.updateTopMarginOnApplyWindowInsets()
            signInButton.setSafeOnClickListener {
                greetingMediator.openGreetingScreen(navController)
            }
        }
    }
}