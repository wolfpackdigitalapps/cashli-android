package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountSubtypeRequestDto(
    val name: String? = null,
    val type: LinkAccountTypeRequestDto? = null
): Parcelable
