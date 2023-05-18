package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.BankAccountSubtypeDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankAccountDto(
    @SerializedName("institution_name")
    val institutionName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("account_subtype")
    val accountSubtype: BankAccountSubtypeDto,
    @SerializedName("account_number_mask")
    val accountNumberMask: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("timestamp")
    val timestamp: String?
) : Parcelable
