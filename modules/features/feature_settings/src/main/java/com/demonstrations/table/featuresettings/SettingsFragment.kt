package com.demonstrations.table.featuresettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.demonstrations.table.featuresettings.databinding.FragmentSettingsBinding
import com.demostration.table.basetable.base.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private lateinit var viewModel: SettingsViewModel

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentSettingsBinding.inflate(inflater, parent, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }
}