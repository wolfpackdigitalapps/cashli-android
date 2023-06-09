package com.wolfpackdigital.cashli.presentation.entities

import android.app.AlertDialog
import androidx.annotation.StringRes

data class PopupConfig(
    val titleIdOrString: Any?,
    val imageId: Int,
    val contentIdOrString: Any? = null,
    val timerCount: Long? = null,
    val isCloseVisible: Boolean = true,
    @StringRes val secondaryContent: Int? = null,
    val buttonPrimaryId: Int? = null,
    val buttonSecondaryId: Int? = null,
    val buttonPrimaryEnabled: Boolean = true,
    val buttonSecondaryEnabled: Boolean = true,
    val buttonPrimaryClick: () -> Unit = {},
    val buttonSecondaryClick: () -> Unit = {},
    val buttonCloseClick: () -> Unit = {},
    val otherAction: (AlertDialog) -> Unit = {},
    val isOtherActionInstant: Boolean = false,
    val contentFormatArgs: Array<Any>? = null,
    val isSecondaryContentBold: Boolean = false
)
