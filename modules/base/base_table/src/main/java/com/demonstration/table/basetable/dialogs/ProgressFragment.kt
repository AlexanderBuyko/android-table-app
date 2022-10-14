package com.demonstration.table.basetable.dialogs

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.demonstration.table.coreapi.models.Config
import com.demonstration.table.basetable.R
import com.demonstration.table.basetable.databinding.DialogProgressBinding
import com.demonstration.baseui.widgets.extentions.setSafeOnClickListener

class ProgressFragment : DialogFragment(), CapableOfCompleting {

    private var _binding: DialogProgressBinding? = null
    val binding: DialogProgressBinding
        get() = checkNotNull(_binding) { "_binding property is null" }

    private val args: ProgressFragmentArgs by navArgs()
    private val config: Config
        get() = args.config

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.progress_background)
        _binding = DialogProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rootContainer
                .layoutTransition
                .enableTransitionType(LayoutTransition.CHANGING)
            confirmationButton.setSafeOnClickListener {
                config.onConfirmation?.invoke()
            }
            cancellationButton.isVisible = config.cancelable
            cancellationButton.setSafeOnClickListener {
                config.onCancellation?.invoke()
            }
            message.text = config.message
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setupDoneIcon() {
        val bounds = binding.progress.indeterminateDrawable.bounds
        context?.let { ContextCompat.getDrawable(it, R.drawable.ic_done) }
            .also {
                binding.progress.setIndeterminateDrawableTiled(it)
                binding.progress.indeterminateDrawable.bounds = bounds
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // According to Google we have to nullify the binding here
        // https://developer.android.com/topic/libraries/view-binding#fragments
        _binding = null
    }

    override fun updateDialogMessage(message: String) {
        with(binding) {
            container.isVisible = true
            this.message.text = message
            setupDoneIcon()
        }
    }
}

interface CapableOfCompleting {
    fun updateDialogMessage(message: String)
}