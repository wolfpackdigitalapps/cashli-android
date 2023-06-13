package com.wolfpackdigital.cashli.presentation.language

import androidx.lifecycle.LiveData
import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class ChooseLanguageViewModel : BaseViewModel(), PersistenceService {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    fun setLanguageAndNavigateToOnboarding(lang: Language) {
        language = lang
        _cmd.value = Command.ChangeAppLocale(lang.toString())
        _baseCmd.value = BaseCommand.PerformNavAction(
            ChooseLanguageFragmentDirections.actionChooseLanguageFragmentToOnboardingFragment()
        )
    }

    sealed class Command {
        data class ChangeAppLocale(val language: String) : Command()
    }
}
