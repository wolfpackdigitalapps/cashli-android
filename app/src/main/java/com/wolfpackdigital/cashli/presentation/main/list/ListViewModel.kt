package com.wolfpackdigital.cashli.presentation.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.wolfpackdigital.cashli.domain.entities.MockItem
import com.wolfpackdigital.cashli.domain.usecases.GetMockListUseCase
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.successOr
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import kotlinx.coroutines.Dispatchers

class ListViewModel(
    getMockListUseCase: GetMockListUseCase
) : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command>
        get() = _cmd

    val mockList: LiveData<List<MockItem>> = getMockListUseCase(viewModelScope, Unit, Dispatchers.IO).map {
        it.successOr(listOf())
    }

    sealed class Command // {
    // class ShowError(val message: String) : Command()
    // }
}
