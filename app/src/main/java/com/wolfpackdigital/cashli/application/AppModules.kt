package com.wolfpackdigital.cashli.application

import com.wolfpackdigital.cashli.data.mappers.RefreshTokenRequestDtoToRefreshTokenRequestMapper
import com.wolfpackdigital.cashli.data.mappers.RefreshTokenRequestToRefreshTokenRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.RegistrationIdentifierChannelDtoToRegistrationIdentifierChannelMapper
import com.wolfpackdigital.cashli.data.mappers.RegistrationIdentifierChannelToRegistrationIdentifierChannelDtoMapper
import com.wolfpackdigital.cashli.data.mappers.RegistrationIdentifiersRequestDtoToRegistrationIdentifiersRequestMapper
import com.wolfpackdigital.cashli.data.mappers.RegistrationIdentifiersRequestToRegistrationIdentifiersRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.TokenDtoToTokenMapper
import com.wolfpackdigital.cashli.data.mappers.TokenToTokenDtoMapper
import com.wolfpackdigital.cashli.data.patternMatchers.EmailPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.PasswordPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.PhoneNumberPatternMatcherImpl
import com.wolfpackdigital.cashli.data.remote.api.common.ApiProvider
import com.wolfpackdigital.cashli.data.repositories.AuthRepositoryImpl
import com.wolfpackdigital.cashli.domain.abstractions.PatternMatcher
import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.domain.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.domain.usecases.GetOnboardingStepsUseCase
import com.wolfpackdigital.cashli.domain.usecases.RefreshTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.SubmitRegistrationIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateBlankFieldUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateChoosePasswordFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateEmailUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateEqualFieldsUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePasswordLengthUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePasswordUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePhoneNumberUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateSignInFormUseCase
import com.wolfpackdigital.cashli.presentation.account.AccountViewModel
import com.wolfpackdigital.cashli.presentation.auth.signin.welcome.SignInViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.choosePassword.ChoosePasswordViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.createProfile.CreateProfileViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.informative.InformativeViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.phoneNumber.PhoneNumberViewModel
import com.wolfpackdigital.cashli.presentation.auth.signup.validateCode.ValidateCodeViewModel
import com.wolfpackdigital.cashli.presentation.home.HomeViewModel
import com.wolfpackdigital.cashli.presentation.language.ChooseLanguageViewModel
import com.wolfpackdigital.cashli.presentation.linkBank.informative.LinkBankAccountInformativeViewModel
import com.wolfpackdigital.cashli.presentation.main.MainActivityViewModel
import com.wolfpackdigital.cashli.presentation.more.MoreViewModel
import com.wolfpackdigital.cashli.presentation.onboarding.OnboardingViewModel
import com.wolfpackdigital.cashli.presentation.onboarding.step.OnboardingStepViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val EMAIL_PATTERN_MATCHER = "email_pattern_matcher"
private const val PASSWORD_PATTERN_MATCHER = "password_pattern_matcher"
private const val PHONE_NUMBER_PATTERN_MATCHER = "phone_number_pattern_matcher"

object AppModules {
    private val viewModels = module {
        viewModel { MainActivityViewModel() }
        viewModel { OnboardingViewModel(get()) }
        viewModel { LinkBankAccountInformativeViewModel() }
        viewModel { ChooseLanguageViewModel() }
        viewModel { (model: OnboardingStep) -> OnboardingStepViewModel(model) }
        viewModel { InformativeViewModel() }
        viewModel { PhoneNumberViewModel(get()) }
        viewModel { ChoosePasswordViewModel(get()) }
        viewModel { SignInViewModel(get()) }
        viewModel { HomeViewModel() }
        viewModel { AccountViewModel() }
        viewModel { MoreViewModel() }
        viewModel { (codeReceivedViaType: CodeReceivedViaType) ->
            ValidateCodeViewModel(
                codeReceivedViaType
            )
        }
        viewModel { CreateProfileViewModel() }
    }

    private val apiModule = module {
        single { ApiProvider.provideAuthApi() }
    }

    private val repoModule = module {
        single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }
    }

    private val patternsModule = module {
        single<PatternMatcher>(named(EMAIL_PATTERN_MATCHER)) { EmailPatternMatcherImpl() }
        single<PatternMatcher>(named(PASSWORD_PATTERN_MATCHER)) { PasswordPatternMatcherImpl() }
        single<PatternMatcher>(named(PHONE_NUMBER_PATTERN_MATCHER)) { PhoneNumberPatternMatcherImpl() }
    }

    private val mappersModule = module {
        factory { TokenDtoToTokenMapper() }
        factory { TokenToTokenDtoMapper() }
        factory { RefreshTokenRequestToRefreshTokenRequestDtoMapper() }
        factory { RefreshTokenRequestDtoToRefreshTokenRequestMapper() }
        factory { RegistrationIdentifierChannelToRegistrationIdentifierChannelDtoMapper() }
        factory { RegistrationIdentifierChannelDtoToRegistrationIdentifierChannelMapper() }
        factory { RegistrationIdentifiersRequestDtoToRegistrationIdentifiersRequestMapper(get()) }
        factory { RegistrationIdentifiersRequestToRegistrationIdentifiersRequestDtoMapper(get()) }
    }

    private val useCases = module {
        single { SubmitRegistrationIdentifiersUseCase(get()) }
        single { RefreshTokenUseCase(get()) }
        single { GetOnboardingStepsUseCase() }
        single { ValidateBlankFieldUseCase() }
        single { ValidateEqualFieldsUseCase() }
        single { ValidatePasswordLengthUseCase() }
        single { ValidateEmailUseCase(get(named(EMAIL_PATTERN_MATCHER))) }
        single { ValidatePasswordUseCase(get(named(PASSWORD_PATTERN_MATCHER))) }
        single { ValidatePhoneNumberUseCase(get(named(PHONE_NUMBER_PATTERN_MATCHER))) }
        single { ValidateSignInFormUseCase(get(), get(), get(), get(), get()) }
        single { ValidateChoosePasswordFormUseCase(get(), get(), get()) }
    }

    val modules = listOf(viewModels, apiModule, repoModule, mappersModule, useCases, patternsModule)
}
