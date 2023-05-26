package com.wolfpackdigital.cashli.presentation.language

import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class ChooseLanguageViewModel : BaseViewModel(), PersistenceService {

    fun setLanguageAndNavigateToOnboarding(lang: Language) {
        language = lang
        _baseCmd.value = BaseCommand.PerformNavAction(
            ChooseLanguageFragmentDirections.actionChooseLanguageFragmentToOnboardingFragment()
        )
    }
}
