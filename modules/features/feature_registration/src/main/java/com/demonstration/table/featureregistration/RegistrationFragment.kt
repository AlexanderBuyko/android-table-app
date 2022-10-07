package com.demonstration.table.featureregistration

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.demonstration.table.coreapi.holders.ActivityProvidersHolder
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featurehomeapi.HomeMediator
import com.demonstration.table.featureregistration.databinding.FragmentRegistrationBinding
import com.demonstration.table.featuresigninapi.SignInMediator
import com.demostration.table.basetable.base.BaseFragment
import com.example.baseui.extentions.*
import javax.inject.Inject
import com.example.baseui.R as baseR


class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    @Inject
    lateinit var signInMediator: SignInMediator

    @Inject
    lateinit var homeMediator: HomeMediator

    @Inject
    lateinit var navController: NavController

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentRegistrationBinding.inflate(inflater, parent, false)
    }

    private lateinit var viewModel: RegistrationViewModel

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
        RegistrationComponent
            .create(
                (requireActivity().application as AppProvidersHolder).getAggregatingProvider(),
                (requireActivity() as ActivityProvidersHolder).getActivityAggregatingProvider()
            )
            .inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViews() {
        with(binding) {
            title.updateTopMarginOnApplyWindowInsets()
            rootContainer.setOnClickListener {
                context?.hideKeyboard(it)
                phoneNumber.clearFocusIfFocused()
                email.clearFocusIfFocused()
                password.clearFocusIfFocused()
            }

            with(passwordContainer) {
                isEndIconVisible = false
                isCounterEnabled = false
                val colorInt = context.getColorFromAttr(android.R.attr.colorControlNormal)
                val colorStateList = ColorStateList.valueOf(colorInt)
                setEndIconTintList(colorStateList)
            }
            with(password) {
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
                    passwordContainer.apply {
                        isEndIconVisible = hasFocus
                        isCounterEnabled = hasFocus
                    }
                }
            }

            signUpViaGoogle.setSafeOnClickListener {
                Toast.makeText(context, "Sign up via Google", Toast.LENGTH_SHORT).show()
            }
            signUpViaGitHub.setSafeOnClickListener {
                Toast.makeText(context, "Sign up via Github", Toast.LENGTH_SHORT).show()
            }
            signUpViaFacebook.setSafeOnClickListener {
                Toast.makeText(context, "Sign up via Facebook", Toast.LENGTH_SHORT).show()
            }
            createAccountButton.setSafeOnClickListener {
                homeMediator.openHomeScreen(navController)
            }

            bottomSignature.addSpannableParts(
                firstSpannablePart(),
                secondSpannablePart()
            )
        }
    }

    private fun firstSpannablePart(): SpannablePart {
        return SpannablePart(
            getString(R.string.register_have_account_signature),
            baseR.style.Theme_Table_TextAppearance_UI14R
        )
    }

    private fun secondSpannablePart(): SpannablePart {
        return SpannablePart(
            getString(R.string.register_log_in_signature),
            baseR.style.Theme_Table_TextAppearance_UI14SB
        ) {
            signInMediator.openSignInScreen(navController)
        }
    }
}