package com.demonstration.table.basetable.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    val binding: T
        get() = checkNotNull(_binding) { "_binding property is null" }

    protected abstract val bindingProvider: (inflater: LayoutInflater, parent: ViewGroup?) -> T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingProvider.invoke(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // According to Google we have to nullify the binding here
        // https://developer.android.com/topic/libraries/view-binding#fragments
        _binding = null
    }
}
