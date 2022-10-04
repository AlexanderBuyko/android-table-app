package com.demonstration.table.featuresignin.recovery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featuresignin.SignInComponent
import com.demonstration.table.featuresignin.databinding.FragmentRecoveryBinding
import com.demostration.table.basetable.base.BaseFragment
import com.example.baseui.extentions.setSafeOnClickListener
import javax.inject.Inject


class RecoveryFragment : BaseFragment<FragmentRecoveryBinding>() {

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentRecoveryBinding.inflate(inflater, parent,false)
    }

    private lateinit var viewModel: RecoveryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initDagger()
    }

    private fun initDagger() {
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
            vMbSignIn.setSafeOnClickListener {
                requireActivity().apply {
                    val intent = packageManager.getLaunchIntentForPackage(packageName)
                    val componentName = intent?.component
                    val mainIntent = Intent.makeRestartActivityTask(componentName)
                    startActivity(mainIntent)
                    Runtime.getRuntime().exit(0)
                }
            }
        }
    }

    companion object {
        val TAG: String = RecoveryFragment::class.java.name

        fun newInstance() = RecoveryFragment()
    }
}