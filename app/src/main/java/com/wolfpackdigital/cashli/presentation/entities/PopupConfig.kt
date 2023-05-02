package com.wolfpackdigital.cashli.presentation.entities

import android.app.AlertDialog

data class PopupConfig(
    var titleId: Int,
    var contentIdOrString: Any? = null,
    var imageId: Int,
    var timerCount: Long? = null,
    var isCloseVisible: Boolean = true,
    var buttonPrimaryId: Int? = null,
    var buttonSecondaryId: Int? = null,
    var buttonPrimaryClick: () -> Unit = {},
    var buttonSecondaryClick: () -> Unit = {},
    var buttonCloseClick: () -> Unit = {},
    var otherAction: (AlertDialog) -> Unit = {},
    var isOtherActionInstant: Boolean = false,
    var contentFormatArgs: Array<Any>? = null
)
