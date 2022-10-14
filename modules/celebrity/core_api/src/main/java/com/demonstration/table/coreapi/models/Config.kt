package com.demonstration.table.coreapi.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Config private constructor(
    val message: String? = null,
    val onConfirmation: (() -> Unit)?,
    val cancelable: Boolean,
    val onCancellation: (() -> Unit)?
) : Parcelable {

    data class Builder(
        private var message: String? = null,
        private var onConfirmation: (() -> Unit)? = null,
        private var cancelable: Boolean = true,
        private var onCancellation: (() -> Unit)? = null
    ) {
        fun message(message: String) = apply { this.message = message }
        fun onConfirmation(onConfirmation: () -> Unit) =
            apply { this.onConfirmation = onConfirmation }

        fun cancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }
        fun onCancellation(onCancellation: () -> Unit) =
            apply { this.onCancellation = onCancellation }

        fun build(): Config {
            return Config(message, onConfirmation, cancelable, onCancellation)
        }
    }
}