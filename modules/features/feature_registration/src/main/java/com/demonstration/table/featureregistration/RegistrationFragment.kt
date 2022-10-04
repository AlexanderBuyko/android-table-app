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
            lClContainer.setOnClickListener {
                context?.hideKeyboard(it)
                vEtPhoneNumber.clearFocusIfFocused()
                vEtEmail.clearFocusIfFocused()
                vEtPassword.clearFocusIfFocused()
            }

            with(lTilPasswordContainer) {
                isEndIconVisible = false
                isCounterEnabled = false
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
                        isCounterEnabled = hasFocus
                    }
                }
            }

            vIvSignUpViaGoogle.setSafeOnClickListener {
                Toast.makeText(context, "Sign up via Google", Toast.LENGTH_SHORT).show()
            }
            vIvSignUpViaGithub.setSafeOnClickListener {
                Toast.makeText(context, "Sign up via Github", Toast.LENGTH_SHORT).show()
            }
            vIvSignUpViaFacebook.setSafeOnClickListener {
                Toast.makeText(context, "Sign up via Facebook", Toast.LENGTH_SHORT).show()
            }
            vMbCreateAccount.setSafeOnClickListener {
                Toast.makeText(context, "Create account clicked", Toast.LENGTH_SHORT).show()
            }

            vTvBottomSignature.addSpannableParts(
                firstSpannablePart(),
                secondSpannablePart()
            )
        }
    }

    private fun firstSpannablePart(): SpannablePart {
        return SpannablePart(
            getString(R.string.register_have_account_signature),
            baseR.style.Theme_Ava_TextAppearance_UI14R
        )
    }

    private fun secondSpannablePart(): SpannablePart {
        return SpannablePart(
            getString(R.string.register_log_in_signature),
            baseR.style.Theme_Ava_TextAppearance_UI14SB
        ) {
            signInMediator.openSignInScreen(navController)
        }
    }
}