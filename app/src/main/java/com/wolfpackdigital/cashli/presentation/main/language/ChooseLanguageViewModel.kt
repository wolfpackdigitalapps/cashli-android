package com.wolfpackdigital.cashli.presentation.main.language

import com.wolfpackdigital.cashli.domain.entities.enums.Languages
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistentService

class ChooseLanguageViewModel : BaseViewModel(), PersistentService {

    fun setLanguageAndNavigateToOnboarding(lang: Languages) {
        language = when (lang) {
            Languages.ENGLISH -> Languages.ENGLISH.lang
            Languages.SPANISH -> Languages.SPANISH.lang
            Languages.HAITI -> Languages.HAITI.lang
        }
        // TODO: Implement the navigation to onboarding screen
    }
}
