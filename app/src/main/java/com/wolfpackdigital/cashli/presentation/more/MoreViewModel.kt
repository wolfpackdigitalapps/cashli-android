package com.wolfpackdigital.cashli.presentation.more

import androidx.lifecycle.LiveData
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class MoreViewModel : BaseViewModel(), PersistenceService {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    val firstName = userProfile?.firstName
    val lastName = userProfile?.lastName

    sealed class Command
}
