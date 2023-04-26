package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountLocalizedBalanceRequestDto(
    val available: String? = null,
    val current: String? = null,
): Parcelable
