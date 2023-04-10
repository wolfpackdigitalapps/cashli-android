package com.wolfpackdigital.cashli.application

import com.wolfpackdigital.cashli.data.mappers.MockItemDtoToMockItemMapper
import com.wolfpackdigital.cashli.data.mappers.MockItemToMockItemDtoMapper
import com.wolfpackdigital.cashli.data.remote.api.common.ApiProvider
import com.wolfpackdigital.cashli.data.repositories.MockRepositoryImpl
import com.wolfpackdigital.cashli.domain.abstractions.MockRepository
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.domain.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.domain.usecases.GetOnboardingStepsUseCase
import com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.confirmOneTimePassword.ConfirmOneTimePasswordViewModel
import com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.requestCode.RequestCodeViewModel
import com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.resetPassword.ResetPasswordViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.choosePassword.ChoosePasswordViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.createProfile.CreateProfileViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.informative.InformativeViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.phoneNumber.PhoneNumberViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.validateCode.ValidateCodeViewModel
import com.wolfpackdigital.cashli.presentation.language.ChooseLanguageViewModel
import com.wolfpackdigital.cashli.presentation.main.MainActivityViewModel
import com.wolfpackdigital.cashli.presentation.onboarding.OnboardingViewModel
import com.wolfpackdigital.cashli.presentation.onboarding.step.OnboardingStepViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModules {
    private val viewModels = module {
        viewModel { MainActivityViewModel() }
        viewModel { OnboardingViewModel(get()) }
        viewModel { ChooseLanguageViewModel() }
        viewModel { (model: OnboardingStep) -> OnboardingStepViewModel(model) }
        viewModel { InformativeViewModel() }
        viewModel { PhoneNumberViewModel() }
        viewModel { ChoosePasswordViewModel() }
        viewModel { (codeReceivedViaType: CodeReceivedViaType) ->
            ValidateCodeViewModel(
                codeReceivedViaType
            )
        }
        viewModel { CreateProfileViewModel() }
        viewModel { ResetPasswordViewModel() }
        viewModel { RequestCodeViewModel() }
        viewModel { (phoneNumberOrEmail: String ) ->
            ConfirmOneTimePasswordViewModel(
                phoneNumberOrEmail
            ) }
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
        single { GetOnboardingStepsUseCase() }
    }

    val modules = listOf(viewModels, apiModule, repoModule, mappersModule, useCases)
}
