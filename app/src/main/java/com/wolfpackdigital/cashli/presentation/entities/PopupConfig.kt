package com.wolfpackdigital.cashli.presentation.entities

import com.wolfpackdigital.cashli.shared.utils.views.PopupDialog

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
    var otherAction: (PopupDialog) -> Unit = {},
    var contentFormatArgs: Array<Any>? = null
)
