package com.wolfpackdigital.cashli.presentation.language

import com.wolfpackdigital.cashli.domain.entities.enums.Languages
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistentService

class ChooseLanguageViewModel : BaseViewModel(), PersistentService {

    fun setLanguageAndNavigateToOnboarding(lang: Languages) {
        language = lang
        _baseCmd.value = BaseCommand.PerformNavAction(
            ChooseLanguageFragmentDirections.actionChooseLanguageFragmentToOnboardingFragment()
        )
    }
}
