package com.demonstration.table.featurecomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demonstration.table.featurecomponents.R
import com.demonstration.table.coreapi.holders.ProvidersHolder

class ComponentsFragment : Fragment() {

    companion object {
        fun newInstance() = ComponentsFragment()
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
        ComponentsComponent.create((context as ProvidersHolder).getAggregatingProvider())
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_components, container, false)
    }
}