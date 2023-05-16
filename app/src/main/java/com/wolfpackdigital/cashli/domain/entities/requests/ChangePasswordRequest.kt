package com.wolfpackdigital.cashli.domain.entities.requests

data class ChangePasswordRequest(
    val password: String,
    val newPassword: String,
    val newPasswordConfirmation: String
)