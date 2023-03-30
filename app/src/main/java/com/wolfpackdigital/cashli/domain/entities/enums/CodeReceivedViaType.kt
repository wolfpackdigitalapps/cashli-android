package com.wolfpackdigital.cashli.domain.entities.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class CodeReceivedViaType : Parcelable {
    SMS, EMAIL
}
