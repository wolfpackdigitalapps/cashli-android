package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.ValidationResult

class ValidateChangePasswordPasswordFormUseCase(
    private val validateChoosePasswordFormUseCase: ValidateChoosePasswordFormUseCase
) {

    operator fun invoke(
        currentPassword: String?,
        newPassword: String?,
        confirmNewPassword: String?
    ): ValidationResult {
        val validNewPassword = validateChoosePasswordFormUseCase(newPassword, confirmNewPassword)
        return when {
            !validNewPassword.successful -> validNewPassword

            currentPassword == newPassword ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.change_password_passwords_match_error
                )

            else -> ValidationResult(successful = true)
        }
    }
}
