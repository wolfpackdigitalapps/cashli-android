package com.wolfpackdigital.cashli.application

import com.wolfpackdigital.cashli.data.mappers.MockItemDtoToMockItemMapper
import com.wolfpackdigital.cashli.data.mappers.MockItemToMockItemDtoMapper
import com.wolfpackdigital.cashli.data.remote.api.common.ApiProvider
import com.wolfpackdigital.cashli.data.repositories.MockRepositoryImpl
import com.wolfpackdigital.cashli.domain.abstractions.MockRepository
import com.wolfpackdigital.cashli.domain.usecases.GetMockListUseCase
import com.wolfpackdigital.cashli.presentation.main.MainActivityViewModel
import com.wolfpackdigital.cashli.presentation.main.language.ChooseLanguageViewModel
import com.wolfpackdigital.cashli.presentation.main.list.ListViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.InformativeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModules {
    private val viewModels = module {
        viewModel { MainActivityViewModel() }
        viewModel { ChooseLanguageViewModel() }
        viewModel { InformativeViewModel() }
        viewModel { ListViewModel(get()) }
    }

    private val apiModule = module {
        single { ApiProvider.provideMockAPI() }
    }

    private val repoModule = module {
        single<MockRepository> { MockRepositoryImpl(get(), get()) }
    }

    private val mappersModule = module {
        factory { MockItemDtoToMockItemMapper() }
        factory { MockItemToMockItemDtoMapper() }
    }

    private val useCases = module {
        single { GetMockListUseCase(get()) }
    }

    val modules = listOf(viewModels, apiModule, repoModule, mappersModule, useCases)
}
