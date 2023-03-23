package com.wolfpackdigital.cashli.presentation.main.language

import com.wolfpackdigital.cashli.shared.Constants.ENGLISH
import com.wolfpackdigital.cashli.shared.Constants.HAITI
import com.wolfpackdigital.cashli.shared.Constants.SPANISH
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistentService

class ChooseLanguageViewModel : BaseViewModel(), PersistentService {

    fun setLanguageAndNavigateToOnboarding(lang: String) {
        when (lang) {
            ENGLISH -> language = ENGLISH
            SPANISH -> language = SPANISH
            HAITI -> language = HAITI
        }
        // TODO: Implement the navigation to onboarding screen
    }
}
