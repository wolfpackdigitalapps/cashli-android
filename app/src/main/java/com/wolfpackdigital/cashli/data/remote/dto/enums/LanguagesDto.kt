package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class LanguagesDto : Parcelable {
    @SerializedName("en")
    ENGLISH,
    @SerializedName("es")
    SPANISH,
    @SerializedName("ht")
    HAITI
}
