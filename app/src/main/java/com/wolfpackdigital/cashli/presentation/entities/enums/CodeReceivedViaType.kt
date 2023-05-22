package com.wolfpackdigital.cashli.presentation.entities.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class CodeReceivedViaType : Parcelable {
    SMS, EMAIL
}
