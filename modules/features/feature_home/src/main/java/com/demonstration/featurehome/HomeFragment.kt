package com.demonstration.featurehome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.demonstration.featurehome.databinding.FragmentHomeBinding
import com.demostration.table.basetable.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentHomeBinding.inflate(inflater, parent, false)
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }
}