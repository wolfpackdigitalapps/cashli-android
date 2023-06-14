package com.wolfpackdigital.cashli.presentation.language

import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel

class ChooseLanguageViewModel : BaseViewModel() {

    fun setLanguageAndNavigateToOnboarding(lang: Language) {
        language = lang
        _baseCmd.value = BaseCommand.PerformNavAction(
            ChooseLanguageFragmentDirections.actionChooseLanguageFragmentToOnboardingFragment()
        )
    }
}
