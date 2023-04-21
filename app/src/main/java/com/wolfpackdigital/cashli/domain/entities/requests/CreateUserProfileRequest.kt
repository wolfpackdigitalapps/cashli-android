package com.wolfpackdigital.cashli.domain.entities.requests

data class CreateUserProfileRequest(
    val userProfileRequest: UserProfileRequest,
    val identifiersTokenRequest: IdentifiersTokenRequest
)
