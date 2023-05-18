package com.wolfpackdigital.cashli.application

import com.wolfpackdigital.cashli.data.mappers.BankAccountDtoToBankAccountMapper
import com.wolfpackdigital.cashli.data.mappers.BankAccountSubtypeDtoToBankAccountSubtypeMapper
import com.wolfpackdigital.cashli.data.mappers.BankAccountSubtypeToBankAccountSubtypeDtoMapper
import com.wolfpackdigital.cashli.data.mappers.BankAccountToBankAccountDtoMapper
import com.wolfpackdigital.cashli.data.mappers.BankTokenDtoToBankTokenMapper
import com.wolfpackdigital.cashli.data.mappers.BankTokenToBankTokenDtoMapper
import com.wolfpackdigital.cashli.data.mappers.BankTransactionDtoToBankTransactionMapper
import com.wolfpackdigital.cashli.data.mappers.BankTransactionToBankTransactionDtoMapper
import com.wolfpackdigital.cashli.data.mappers.ChangePasswordRequestToChangePasswordRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.CompleteLinkBankAccountRequestDtoToCompleteLinkBankAccountRequestMapper
import com.wolfpackdigital.cashli.data.mappers.CompleteLinkBankAccountRequestToCompleteLinkBankAccountRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.CreateUserProfileRequestDtoToCreateUserProfileRequestMapper
import com.wolfpackdigital.cashli.data.mappers.CreateUserProfileRequestToCreateUserProfileRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.EligibilityStatusDtoToEligibilityStatusMapper
import com.wolfpackdigital.cashli.data.mappers.EligibilityStatusToEligibilityStatusDtoMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifierChannelDtoToIdentifierChannelMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifierChannelToIdentifierChannelDtoMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifierTokenDtoToIdentifierTokenMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifierTokenToIdentifierTokenDtoMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifiersCodeValidationRequestDtoToIdentifiersCodeValidationRequestMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifiersCodeValidationRequestToIdentifiersCodeValidationRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifiersRequestDtoToIdentifiersRequestMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifiersRequestToIdentifiersRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifiersTokenRequestDtoToIdentifiersTokenRequestMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifiersTokenRequestToIdentifiersTokenRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.LanguagesDtoToLanguagesMapper
import com.wolfpackdigital.cashli.data.mappers.LanguagesToLanguagesDtoMapper
import com.wolfpackdigital.cashli.data.mappers.LinkAccountMetadataRequestDtoToLinkAccountMetadataRequestMapper
import com.wolfpackdigital.cashli.data.mappers.LinkAccountMetadataRequestToLinkAccountMetadataRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.PasswordIdentifierTokenDtoToPasswordIdentifierTokenMapper
import com.wolfpackdigital.cashli.data.mappers.PasswordIdentifierTokenToPasswordIdentifierTokenDtoMapper
import com.wolfpackdigital.cashli.data.mappers.RefreshTokenRequestDtoToRefreshTokenRequestMapper
import com.wolfpackdigital.cashli.data.mappers.RefreshTokenRequestToRefreshTokenRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.ResetPasswordRequestDtoToResetPasswordRequestMapper
import com.wolfpackdigital.cashli.data.mappers.ResetPasswordRequestToResetPasswordRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.SignInRequestDtoToSignInRequestMapper
import com.wolfpackdigital.cashli.data.mappers.SignInRequestToSignInRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.SingleDataRequestDtoToSingleDataRequestMapper
import com.wolfpackdigital.cashli.data.mappers.SingleDataRequestToSingleDataRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.TokenDtoToTokenMapper
import com.wolfpackdigital.cashli.data.mappers.TokenToTokenDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UpdateIdentifiersCodeValidationRequestDtoToUpdateIdentifiersCodeValidationRequestMapper
import com.wolfpackdigital.cashli.data.mappers.UpdateIdentifiersCodeValidationRequestToUpdateIdentifiersCodeValidationRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UpdateUserProfileRequestDtoToUserProfileRequestMapper
import com.wolfpackdigital.cashli.data.mappers.UpdateUserProfileRequestToUserProfileRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UserProfileDtoToUserProfileMapper
import com.wolfpackdigital.cashli.data.mappers.UserProfileRequestDtoToUserProfileRequestMapper
import com.wolfpackdigital.cashli.data.mappers.UserProfileRequestToUserProfileRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UserProfileToUserProfileDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingDtoToUserSettingMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingToUserSettingDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UserSignInRequestDtoToUserSignInRequestMapper
import com.wolfpackdigital.cashli.data.mappers.UserSignInRequestToUserSignInRequestDtoMapper
import com.wolfpackdigital.cashli.data.paging.BankTransactionsPagingSource
import com.wolfpackdigital.cashli.data.patternMatchers.CityAndStatePatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.EmailPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.LettersAndCommaPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.NamePatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.PasswordPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.PhoneNumberPatternMatcherImpl
import com.wolfpackdigital.cashli.data.patternMatchers.ZipCodePatternMatcherImpl
import com.wolfpackdigital.cashli.data.remote.api.common.ApiProvider
import com.wolfpackdigital.cashli.data.repositories.AuthRepositoryImpl
import com.wolfpackdigital.cashli.data.repositories.BankRepositoryImpl
import com.wolfpackdigital.cashli.data.repositories.UserRepositoryImpl
import com.wolfpackdigital.cashli.domain.abstractions.PatternMatcher
import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.abstractions.repositories.BankRepository
import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.domain.entities.enums.EditPhoneOrEmail
import com.wolfpackdigital.cashli.domain.usecases.ChangePasswordUseCase
import com.wolfpackdigital.cashli.domain.usecases.CompleteLinkingBankAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GenerateLinkTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetEligibilityStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetOnboardingStepsUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserProfileUseCase
import com.wolfpackdigital.cashli.domain.usecases.LogOutUserUseCase
import com.wolfpackdigital.cashli.domain.usecases.RefreshTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.RegisterDeviceTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.RegisterNewUserUseCase
import com.wolfpackdigital.cashli.domain.usecases.ResetPasswordUseCase
import com.wolfpackdigital.cashli.domain.usecases.SignInUserUseCase
import com.wolfpackdigital.cashli.domain.usecases.SubmitChangeIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.SubmitPasswordIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.SubmitRegistrationIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.UpdateUserProfileUseCase
import com.wolfpackdigital.cashli.domain.usecases.UpdateUserSettingUseCase
import com.wolfpackdigital.cashli.domain.usecases.ValidateCodeByIdentifierUseCase
import com.wolfpackdigital.cashli.domain.usecases.ValidateCodeByPasswordIdentifierUseCase
import com.wolfpackdigital.cashli.domain.usecases.ValidateCodeByUpdateIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateBlankFieldUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateChangePasswordPasswordFormUseCase
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
import com.wolfpackdigital.cashli.presentation.changePassword.ChangePasswordViewModel
import com.wolfpackdigital.cashli.presentation.claimCash.ClaimCashViewModel
import com.wolfpackdigital.cashli.presentation.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.presentation.help.HelpViewModel
import com.wolfpackdigital.cashli.presentation.home.HomeViewModel
import com.wolfpackdigital.cashli.presentation.language.ChooseLanguageViewModel
import com.wolfpackdigital.cashli.presentation.linkBank.ineligible.IneligibleInformativeViewModel
import com.wolfpackdigital.cashli.presentation.linkBank.informative.LinkBankAccountInformativeViewModel
import com.wolfpackdigital.cashli.presentation.main.MainActivityViewModel
import com.wolfpackdigital.cashli.presentation.more.MoreViewModel
import com.wolfpackdigital.cashli.presentation.more.editProfile.EditProfileViewModel
import com.wolfpackdigital.cashli.presentation.more.editProfile.changePhoneOrEmail.ChangePhoneOrEmailViewModel
import com.wolfpackdigital.cashli.presentation.onboarding.OnboardingViewModel
import com.wolfpackdigital.cashli.presentation.onboarding.step.OnboardingStepViewModel
import com.wolfpackdigital.cashli.presentation.quiz.QuizViewModel
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
        viewModel { LinkBankAccountInformativeViewModel(get(), get(), get()) }
        viewModel { ChooseLanguageViewModel() }
        viewModel { (model: OnboardingStep) -> OnboardingStepViewModel(model) }
        viewModel { InformativeViewModel() }
        viewModel { PhoneNumberViewModel(get(), get()) }
        viewModel { ChoosePasswordViewModel(get(), get(), get()) }
        viewModel { SignInViewModel(get(), get(), get()) }
        viewModel { HomeViewModel(get(), get()) }
        viewModel { AccountViewModel() }
        viewModel { MoreViewModel(get()) }
        viewModel { (identifier: String?, codeReceivedViaType: CodeReceivedViaType) ->
            ValidateCodeViewModel(
                identifier, codeReceivedViaType, get(), get()
            )
        }
        viewModel { CreateProfileViewModel(get(), get(), get(), get(), get(), get(), get()) }
        viewModel { (token: String) -> ResetPasswordViewModel(token, get(), get()) }
        viewModel { RequestCodeViewModel(get(), get()) }
        viewModel { (
            phoneNumberOrEmail: String, codeReceivedViaType: CodeReceivedViaType,
            fromEditProfile: Boolean
        ) ->
            ConfirmOneTimePasswordViewModel(
                phoneNumberOrEmail, codeReceivedViaType, fromEditProfile, get(), get(), get(), get()
            )
        }
        viewModel { IneligibleInformativeViewModel() }
        viewModel { ClaimCashViewModel() }
        viewModel { (cashAmount: Float) -> QuizViewModel(cashAmount) }
        viewModel { HelpViewModel() }
        viewModel { ChangePasswordViewModel(get(), get()) }
        viewModel { EditProfileViewModel(get(), get(), get()) }
        viewModel { (editPhoneOrEmail: EditPhoneOrEmail) ->
            ChangePhoneOrEmailViewModel(editPhoneOrEmail, get(), get(), get())
        }
    }

    private val apiModule = module {
        single { ApiProvider.provideAuthApi() }
        single { ApiProvider.provideBankApi() }
        single { ApiProvider.provideUserApi() }
    }

    private val repoModule = module {
        single<AuthRepository> {
            AuthRepositoryImpl(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get()
            )
        }
        single<BankRepository> { BankRepositoryImpl(get(), get(), get(), get()) }
        single<UserRepository> {
            UserRepositoryImpl(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get()
            )
        }
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
        factory { BankTransactionToBankTransactionDtoMapper() }
        factory { BankTransactionDtoToBankTransactionMapper() }
        factory { UserSettingDtoToUserSettingMapper() }
        factory { UserSettingToUserSettingDtoMapper() }
        factory { SingleDataRequestToSingleDataRequestDtoMapper() }
        factory { SingleDataRequestDtoToSingleDataRequestMapper() }
        factory { BankAccountSubtypeDtoToBankAccountSubtypeMapper() }
        factory { BankAccountSubtypeToBankAccountSubtypeDtoMapper() }
        factory { BankAccountToBankAccountDtoMapper(get()) }
        factory { BankAccountDtoToBankAccountMapper(get()) }
        factory { EligibilityStatusDtoToEligibilityStatusMapper() }
        factory { EligibilityStatusToEligibilityStatusDtoMapper() }
        factory { BankTokenToBankTokenDtoMapper() }
        factory { BankTokenDtoToBankTokenMapper() }
        factory { TokenDtoToTokenMapper() }
        factory { TokenToTokenDtoMapper() }
        factory { RefreshTokenRequestToRefreshTokenRequestDtoMapper() }
        factory { RefreshTokenRequestDtoToRefreshTokenRequestMapper() }
        factory { IdentifierChannelToIdentifierChannelDtoMapper() }
        factory { IdentifierChannelDtoToIdentifierChannelMapper() }
        factory { IdentifiersRequestDtoToIdentifiersRequestMapper(get()) }
        factory { IdentifiersRequestToIdentifiersRequestDtoMapper(get()) }
        factory { IdentifierTokenDtoToIdentifierTokenMapper() }
        factory { IdentifierTokenToIdentifierTokenDtoMapper() }
        factory { CreateUserProfileRequestToCreateUserProfileRequestDtoMapper(get(), get()) }
        factory { CreateUserProfileRequestDtoToCreateUserProfileRequestMapper(get(), get()) }
        factory { UserProfileRequestDtoToUserProfileRequestMapper(get()) }
        factory { UserProfileRequestToUserProfileRequestDtoMapper(get()) }
        factory { IdentifiersTokenRequestToIdentifiersTokenRequestDtoMapper() }
        factory { IdentifiersTokenRequestDtoToIdentifiersTokenRequestMapper() }
        factory { IdentifiersCodeValidationRequestToIdentifiersCodeValidationRequestDtoMapper() }
        factory { IdentifiersCodeValidationRequestDtoToIdentifiersCodeValidationRequestMapper() }
        factory { UserProfileToUserProfileDtoMapper(get(), get(), get(), get(), get()) }
        factory { UserProfileDtoToUserProfileMapper(get(), get(), get(), get(), get()) }
        factory { LanguagesToLanguagesDtoMapper() }
        factory { LanguagesDtoToLanguagesMapper() }
        factory { SignInRequestToSignInRequestDtoMapper(get()) }
        factory { SignInRequestDtoToSignInRequestMapper(get()) }
        factory { UserSignInRequestToUserSignInRequestDtoMapper() }
        factory { UserSignInRequestDtoToUserSignInRequestMapper() }
        factory { PasswordIdentifierTokenDtoToPasswordIdentifierTokenMapper() }
        factory { PasswordIdentifierTokenToPasswordIdentifierTokenDtoMapper() }
        factory { ResetPasswordRequestDtoToResetPasswordRequestMapper() }
        factory { ResetPasswordRequestToResetPasswordRequestDtoMapper() }
        factory { CompleteLinkBankAccountRequestToCompleteLinkBankAccountRequestDtoMapper(get()) }
        factory { CompleteLinkBankAccountRequestDtoToCompleteLinkBankAccountRequestMapper(get()) }
        factory { LinkAccountMetadataRequestToLinkAccountMetadataRequestDtoMapper() }
        factory { LinkAccountMetadataRequestDtoToLinkAccountMetadataRequestMapper() }
        factory { ChangePasswordRequestToChangePasswordRequestDtoMapper() }
        factory {
            UpdateIdentifiersCodeValidationRequestToUpdateIdentifiersCodeValidationRequestDtoMapper(
                get()
            )
        }
        factory {
            UpdateIdentifiersCodeValidationRequestDtoToUpdateIdentifiersCodeValidationRequestMapper(
                get()
            )
        }
        factory { UpdateUserProfileRequestDtoToUserProfileRequestMapper(get()) }
        factory { UpdateUserProfileRequestToUserProfileRequestDtoMapper(get()) }
    }

    private val useCases = module {
        single { UpdateUserSettingUseCase(get()) }
        single { RegisterDeviceTokenUseCase(get()) }
        single { GetUserProfileUseCase(get()) }
        single { GetEligibilityStatusUseCase(get()) }
        single { CompleteLinkingBankAccountUseCase(get()) }
        single { GenerateLinkTokenUseCase(get()) }
        single { SubmitRegistrationIdentifiersUseCase(get()) }
        single { RefreshTokenUseCase(get()) }
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
        single { ValidateCodeByIdentifierUseCase(get()) }
        single { RegisterNewUserUseCase(get()) }
        single { SignInUserUseCase(get()) }
        single { SubmitPasswordIdentifiersUseCase(get()) }
        single { ValidateCodeByPasswordIdentifierUseCase(get()) }
        single { ResetPasswordUseCase(get()) }
        single { LogOutUserUseCase(get()) }
        single { ChangePasswordUseCase(get()) }
        single { ValidateChangePasswordPasswordFormUseCase(get()) }
        single { SubmitChangeIdentifiersUseCase(get()) }
        single { ValidateCodeByUpdateIdentifiersUseCase(get()) }
        single { UpdateUserProfileUseCase(get()) }
    }

    private val pagingSources = module {
        factory { BankTransactionsPagingSource(get()) }
    }
    val modules = listOf(
        viewModels,
        apiModule,
        repoModule,
        mappersModule,
        useCases,
        patternsModule,
        pagingSources
    )
}
