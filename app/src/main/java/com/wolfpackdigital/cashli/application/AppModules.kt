package com.wolfpackdigital.cashli.application

import com.wolfpackdigital.cashli.data.mappers.MockItemDtoToMockItemMapper
import com.wolfpackdigital.cashli.data.mappers.MockItemToMockItemDtoMapper
import com.wolfpackdigital.cashli.data.patternMatchers.CityAndStatePatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.EmailPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.LettersAndCommaPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.NamePatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.PasswordPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.PhoneNumberPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.ZipCodePatternMatcherImpl
import com.wolfpackdigital.cashli.data.remote.api.common.ApiProvider
import com.wolfpackdigital.cashli.data.repositories.MockRepositoryImpl
import com.wolfpackdigital.cashli.domain.abstractions.MockRepository
import com.wolfpackdigital.cashli.domain.abstractions.PatternMatcher
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.domain.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.domain.usecases.GetOnboardingStepsUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateBlankFieldUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateChoosePasswordFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateCityAndStateFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateCityAndStateUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateEmailUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateEqualFieldsUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateFirstNameFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateLastNameFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateLettersAndCommaUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateNameLengthUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateNameUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePasswordLengthUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePasswordUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePhoneNumberFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePhoneNumberLengthUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePhoneNumberUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateRequestCodeFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateSignInFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateStreetFieldUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateZipCodeUseCase
import com.wolfpackdigital.cashli.presentation.account.AccountViewModel
import com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.confirmOneTimePassword.ConfirmOneTimePasswordViewModel
import com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.requestCode.RequestCodeViewModel
import com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.resetPassword.ResetPasswordViewModel
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
private const val ZIP_CODE_PATTERN_MATCHER = "zip_code_pattern_matcher"
private const val NAME_PATTERN_MATCHER = "name_pattern_matcher"
private const val CITY_AND_STATE_PATTERN_MATCHER = "city_and_state_pattern_matcher"
private const val LETTERS_AND_COMMA_PATTERN_MATCHER = "letters_and_comma_pattern_matcher"

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
        viewModel { CreateProfileViewModel(get(), get(), get(), get(), get(), get()) }
        viewModel { ResetPasswordViewModel(get()) }
        viewModel { RequestCodeViewModel(get()) }
        viewModel { (phoneNumberOrEmail: String) ->
            ConfirmOneTimePasswordViewModel(
                phoneNumberOrEmail
            )
        }
    }

    private val apiModule = module {
        single { ApiProvider.provideMockAPI() }
    }

    private val repoModule = module {
        single<MockRepository> { MockRepositoryImpl(get(), get()) }
    }

    private val patternsModule = module {
        single<PatternMatcher>(named(EMAIL_PATTERN_MATCHER)) { EmailPatternMatcherImpl() }
        single<PatternMatcher>(named(PASSWORD_PATTERN_MATCHER)) { PasswordPatternMatcherImpl() }
        single<PatternMatcher>(named(PHONE_NUMBER_PATTERN_MATCHER)) { PhoneNumberPatternMatcherImpl() }
        single<PatternMatcher>(named(ZIP_CODE_PATTERN_MATCHER)) { ZipCodePatternMatcherImpl() }
        single<PatternMatcher>(named(NAME_PATTERN_MATCHER)) { NamePatternMatcherImpl() }
        single<PatternMatcher>(named(LETTERS_AND_COMMA_PATTERN_MATCHER)) { LettersAndCommaPatternMatcherImpl() }
        single<PatternMatcher>(named(CITY_AND_STATE_PATTERN_MATCHER)) { CityAndStatePatternMatcherImpl() }
    }

    private val mappersModule = module {
        factory { MockItemDtoToMockItemMapper() }
        factory { MockItemToMockItemDtoMapper() }
    }

    private val useCases = module {
        single { GetOnboardingStepsUseCase() }
        single { ValidateBlankFieldUseCase() }
        single { ValidateEqualFieldsUseCase() }
        single { ValidatePasswordLengthUseCase() }
        single { ValidateEmailUseCase(get(named(EMAIL_PATTERN_MATCHER))) }
        single { ValidatePasswordUseCase(get(named(PASSWORD_PATTERN_MATCHER))) }
        single { ValidatePhoneNumberUseCase(get(named(PHONE_NUMBER_PATTERN_MATCHER))) }
        single { ValidateSignInFormUseCase(get(), get(), get(), get(), get(), get()) }
        single { ValidateChoosePasswordFormUseCase(get(), get(), get()) }
        single { ValidateRequestCodeFormUseCase(get(), get(), get(), get()) }
        single { ValidatePhoneNumberLengthUseCase() }
        single { ValidatePhoneNumberFormUseCase(get(), get()) }
        single { ValidateStreetFieldUseCase() }
        single { ValidateZipCodeUseCase(get(named(ZIP_CODE_PATTERN_MATCHER))) }
        single { ValidateNameUseCase(get(named(NAME_PATTERN_MATCHER))) }
        single { ValidateNameLengthUseCase() }
        single { ValidateLastNameFormUseCase(get(), get()) }
        single { ValidateFirstNameFormUseCase(get(), get()) }
        single { ValidateLettersAndCommaUseCase(get(named(LETTERS_AND_COMMA_PATTERN_MATCHER))) }
        single { ValidateCityAndStateUseCase(get(named(CITY_AND_STATE_PATTERN_MATCHER))) }
        single { ValidateCityAndStateFormUseCase(get(), get()) }
    }

    val modules = listOf(viewModels, apiModule, repoModule, mappersModule, useCases, patternsModule)
}
