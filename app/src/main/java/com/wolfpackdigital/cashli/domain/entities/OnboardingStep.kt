package com.wolfpackdigital.cashli.domain.entities

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingStep(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val subtitle: Int? = null,
    @StringRes val content: Int? = null,
    @StringRes val news: Int? = null,
    val isInProgress: Boolean = false
) : Parcelable
