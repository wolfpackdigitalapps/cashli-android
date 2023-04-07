package com.wolfpackdigital.cashli.presentation.entities

data class PopupConfig(
    var titleId: Int,
    var contentIdOrString: Any? = null,
    var imageId: Int,
    var timerCount: Long? = null,
    var isBackVisible: Boolean = true,
    var buttonPrimaryId: Int? = null,
    var buttonSecondaryId: Int? = null,
    var buttonPrimaryClick: () -> Unit = {},
    var buttonSecondaryClick: () -> Unit = {},
    var buttonCloseClick: () -> Unit = {},
    var contentFormatArgs: Array<Any>? = null
)
