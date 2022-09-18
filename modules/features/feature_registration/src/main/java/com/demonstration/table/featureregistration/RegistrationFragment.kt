package com.demonstration.table.featureregistration

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demonstration.table.featureregistration.databinding.FragmentRegistrationBinding
import com.demonstration.table.coreapi.ProvidersHolder
import com.example.baseui.extentions.*
import com.example.baseui.R as baseR


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDaggerComponent()
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initDaggerComponent() {
        RegistrationComponent
            .create((requireActivity().application as ProvidersHolder).getAggregatingProvider())
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
            Toast.makeText(context, "Log In clicked", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }
}