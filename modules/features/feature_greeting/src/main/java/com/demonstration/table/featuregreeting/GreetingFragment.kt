package com.demonstration.table.featuregreeting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.demonstration.table.coreapi.ProvidersHolder
import com.demonstration.table.coreapi.providers.AggregatingProvider
import com.demonstration.table.featuregreeting.databinding.FragmentGreetingBinding
import com.demonstration.table.featureregistrationapi.RegistrationMediator
import com.example.baseui.extentions.setSafeOnClickListener
import javax.inject.Inject

class GreetingFragment : Fragment() {

    @Inject
    lateinit var registrationMediator: RegistrationMediator

    private lateinit var viewModel: GreetingViewModel

    private var _binding: FragmentGreetingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGreetingBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initDagger() {
        GreetingComponent
            .create((requireActivity().application as ProvidersHolder).getAggregatingProvider())
            .inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(GreetingViewModel::class.java)
    }

    private fun setupViews() {
        with(binding) {
            vMbRegister.setSafeOnClickListener {
                registrationMediator.openRegistrationScreen(
                    parentFragmentManager,
                    com.demonstration.table.featurecontainer.R.id.vFragmentContainer
                )
            }
            vMbSignIn.setSafeOnClickListener {
                Toast.makeText(context, "Sign In clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance() = GreetingFragment()
    }
}