package com.demonstration.table.featurecomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.demonstration.table.coreapi.holders.AppProvidersHolder
import com.demonstration.table.featurecomponents.databinding.FragmentComponentsBinding
import com.demostration.table.basetable.base.BaseFragment

class ComponentsFragment : BaseFragment<FragmentComponentsBinding>() {

    override val bindingProvider = { inflater: LayoutInflater, parent: ViewGroup? ->
        FragmentComponentsBinding.inflate(inflater, parent,false)
    }

    private lateinit var viewModel: ComponentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDaggerComponent()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ComponentsViewModel::class.java]
    }

    private fun initDaggerComponent() {
        ComponentsComponent
            .create((requireActivity().application as AppProvidersHolder).getAggregatingProvider())
            .inject(this)
    }

    companion object {
        fun newInstance() = ComponentsFragment()
    }
}