package com.wolfpackdigital.cashli.presentation.auth.signup

import com.wolfpackdigital.cashli.domain.entities.requests.CreateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersTokenRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UserProfileRequest
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet

class SignUpSharedViewModel : BaseViewModel() {

    private var userProfileRequest: UserProfileRequest? = null
    private var identifiersTokenRequest: IdentifiersTokenRequest? = null

    fun initSignUpData() {
        userProfileRequest = UserProfileRequest()
        identifiersTokenRequest = IdentifiersTokenRequest()
    }

    fun clearSignUpData() {
        userProfileRequest = null
        identifiersTokenRequest = null
    }

    fun updatePhoneNumber(phoneNumber: String) {
        userProfileRequest = userProfileRequest?.copy(phoneNumber = phoneNumber)
    }

    fun updatePhoneNumberToken(phoneNumberToken: String) {
        identifiersTokenRequest = identifiersTokenRequest?.copy(phoneNumberToken = phoneNumberToken)
    }

    fun updateEmailToken(emailToken: String) {
        identifiersTokenRequest = identifiersTokenRequest?.copy(emailToken = emailToken)
    }

    fun updateProfile(temporaryUserProfileRequest: UserProfileRequest) {
        userProfileRequest = userProfileRequest?.copy(
            firstName = temporaryUserProfileRequest.firstName,
            lastName = temporaryUserProfileRequest.lastName,
            email = temporaryUserProfileRequest.email,
            street = temporaryUserProfileRequest.street,
            zipCode = temporaryUserProfileRequest.zipCode,
            city = temporaryUserProfileRequest.city,
            state = temporaryUserProfileRequest.state,
            language = temporaryUserProfileRequest.language
        )
    }

    fun updatePassword(password: String) {
        userProfileRequest = userProfileRequest?.copy(password = password)
    }

    fun createUserProfileRequest() = safeLet(
        userProfileRequest, identifiersTokenRequest
    ) { userProfileRequest, identifiersTokenRequest ->
        CreateUserProfileRequest(
            userProfileRequest = userProfileRequest,
            identifiersTokenRequest = identifiersTokenRequest
        )
    }

    fun getUserPhoneNumber() = userProfileRequest?.phoneNumber

    fun getUserEmail() = userProfileRequest?.email
}
