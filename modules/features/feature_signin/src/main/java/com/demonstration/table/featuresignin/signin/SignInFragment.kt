package com.demonstration.table.featuresignin.signin

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import com.demonstration.table.featuresignin.R
import com.demonstration.table.featuresignin.SignInComponent
import com.demonstration.table.featuresignin.databinding.FragmentSignInBinding
import com.demostration.table.basetable.base.BaseFragment
import com.example.baseui.R as baseR
import com.example.baseui.extentions.*
import com.example.baseui.factories.NavOptionsFactory
import javax.inject.Inject

class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    @Inject
    lateinit var registrationMediator: RegistrationMediator

    @Inject
    lateinit var navController: NavController

    @Inject
    lateinit var navOptionsFactory: NavOptionsFactory

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentSignInBinding.inflate(inflater, parent, false)
    }

    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
        initViewModel()
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
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
    }

    private fun setupViews() {
        with(binding) {
            lLlContainer.setOnClickListener {
                context?.hideKeyboard(it)
                vEtPhoneNumber.clearFocusIfFocused()
                vEtPassword.clearFocusIfFocused()
            }
            with(lTilPasswordContainer) {
                isEndIconVisible = false
                val colorInt = context.getColorFromAttr(android.R.attr.colorControlNormal)
                val colorStateList = ColorStateList.valueOf(colorInt)
                setEndIconTintList(colorStateList)
            }
            with(vEtPassword) {
                setOnEditorActionListener { view, actionId, _ ->
                    return@setOnEditorActionListener when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            context?.hideKeyboard(view)
                            view.clearFocus()
                            true
                        }
                        else -> false
                    }
                }
                setOnFocusChangeListener { _, hasFocus ->
                    lTilPasswordContainer.apply {
                        isEndIconVisible = hasFocus
                    }
                }
            }
            vMtvRecovery.setSafeOnClickListener {
                navController.navigate(R.id.recoveryFragment, null, navOptionsFactory.createDefault())
            }
            vMbSignIn.setSafeOnClickListener {

            }
            vMtvRegisterTitle.addSpannableParts(
                firstSpannablePart(),
                secondSpannablePart()
            )
        }
    }

    private fun firstSpannablePart(): SpannablePart {
        return SpannablePart(
            getString(R.string.sign_in_member_signature),
            baseR.style.Theme_Ava_TextAppearance_UI14R
        )
    }

    private fun secondSpannablePart(): SpannablePart {
        return SpannablePart(
            getString(R.string.sign_in_register_signature),
            baseR.style.Theme_Ava_TextAppearance_UI14SB
        ) {
            registrationMediator.openRegistrationScreen(navController)
        }
    }
}