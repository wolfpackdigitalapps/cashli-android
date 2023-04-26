package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountBalanceRequestDto(
    val available: Double? = null,
    val currency: String? = null,
    val current: Double? = null,
    val localized: LinkAccountLocalizedBalanceRequestDto? = null
): Parcelable
