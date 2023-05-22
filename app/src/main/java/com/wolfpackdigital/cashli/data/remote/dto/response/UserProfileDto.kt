package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.AccountStatusDto
import com.wolfpackdigital.cashli.data.remote.dto.enums.EligibilityStatusDto
import com.wolfpackdigital.cashli.data.remote.dto.enums.LanguagesDto
import com.wolfpackdigital.cashli.domain.entities.response.LastMembership
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileDto(
    @SerializedName("id") val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("street") val street: String,
    @SerializedName("zip_code") val zipCode: String,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("language") val language: LanguagesDto = LanguagesDto.ENGLISH,
    @SerializedName("tokens") val tokens: TokenDto?,
    @SerializedName("bank_account_connected") val bankAccountConnected: Boolean,
    @SerializedName("last_membership") val lastMembership: LastMembershipDto?,
    @SerializedName("eligibility_status") val eligibilityStatus: EligibilityStatusDto,
    @SerializedName("bank_account") val bankAccount: BankAccountDto?,
    @SerializedName("user_settings") val userSettings: List<UserSettingDto>?,
    @SerializedName("account_status") val accountStatus: AccountStatusDto
) : Parcelable
