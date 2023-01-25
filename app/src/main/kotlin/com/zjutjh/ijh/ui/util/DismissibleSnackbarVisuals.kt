package com.zjutjh.ijh.ui.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

class DismissibleSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
) : SnackbarVisuals {
    override val withDismissAction: Boolean = true
}
