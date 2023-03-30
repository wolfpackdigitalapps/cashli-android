package com.wolfpackdigital.cashli.presentation.auth.signup

import com.wolfpackdigital.cashli.domain.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel

class InformativeViewModel : BaseViewModel() {

    fun signIn() {
        // TODO
    }

    fun signUp() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            InformativeFragmentDirections.actionInformativeFragmentToValidateCodeFragment(
                CodeReceivedViaType.SMS
            )
        )
    }
}
