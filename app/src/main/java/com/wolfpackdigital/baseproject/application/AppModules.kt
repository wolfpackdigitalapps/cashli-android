package com.wolfpackdigital.baseproject.application

import com.wolfpackdigital.baseproject.data.mappers.MockItemDtoToMockItemMapper
import com.wolfpackdigital.baseproject.data.mappers.MockItemToMockItemDtoMapper
import com.wolfpackdigital.baseproject.presentation.main.MainActivityViewModel
import com.wolfpackdigital.baseproject.domain.usecases.GetMockListUseCase
import com.wolfpackdigital.baseproject.presentation.main.list.ListViewModel
import com.wolfpackdigital.baseproject.presentation.main.splash.SplashScreenViewModel
import com.wolfpackdigital.baseproject.data.remote.api.common.ApiProvider
import com.wolfpackdigital.baseproject.data.repositories.MockRepositoryImpl
import com.wolfpackdigital.baseproject.domain.abstractions.MockRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModules {
    private val viewModels = module {
        viewModel { MainActivityViewModel() }
        viewModel { SplashScreenViewModel() }
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
