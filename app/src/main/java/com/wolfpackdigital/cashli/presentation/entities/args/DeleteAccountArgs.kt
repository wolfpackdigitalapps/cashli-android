package com.wolfpackdigital.cashli.presentation.entities.args

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeleteAccountArgs(
    @IgnoredOnParcel val onDeleteAccount: (String?) -> Unit = {}
) : Parcelable
