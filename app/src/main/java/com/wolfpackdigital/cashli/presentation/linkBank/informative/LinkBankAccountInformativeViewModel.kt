package com.wolfpackdigital.cashli.presentation.linkBank.informative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.domain.usecases.CompleteLinkingBankAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GenerateLinkTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetCashAdvancesLimitsUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.presentation.plaid.LinkPlaidAccountViewModel

class LinkBankAccountInformativeViewModel(
    generateLinkTokenUseCase: GenerateLinkTokenUseCase,
    completeLinkingBankAccountUseCase: CompleteLinkingBankAccountUseCase,
    getCashAdvancesLimitsUseCase: GetCashAdvancesLimitsUseCase
) : LinkPlaidAccountViewModel(
    generateLinkTokenUseCase,
    completeLinkingBankAccountUseCase,
    getCashAdvancesLimitsUseCase
) {

    private val _toolbar = MutableLiveData(
        Toolbar(onBack = ::back)
    )
    val toolbar: LiveData<Toolbar> = _toolbar
}
