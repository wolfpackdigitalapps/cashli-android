package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountTypeRequestDto(
    val name: String? = null
): Parcelable
