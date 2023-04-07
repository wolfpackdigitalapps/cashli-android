package com.wolfpackdigital.cashli.presentation.entities

import com.wolfpackdigital.cashli.shared.utils.Constants.ALPHA_0
import com.wolfpackdigital.cashli.shared.utils.Constants.ALPHA_1
import com.wolfpackdigital.cashli.shared.utils.Constants.FADE_ANIM_DURATION_100

data class AlphaAnimationConfig(
    val startAlpha: Float = ALPHA_1,
    val endAlpha: Float = ALPHA_0,
    val duration: Long = FADE_ANIM_DURATION_100,
    val repeatCountValue: Int? = null,
    val repeatModeValue: Int? = null,
    val actionOnStart: () -> Unit = {},
    val actionOnEnd: () -> Unit = {},
    val actionOnRepeat: () -> Unit = {}
)
