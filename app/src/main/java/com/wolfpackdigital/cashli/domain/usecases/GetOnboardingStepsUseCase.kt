package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class GetOnboardingStepsUseCase : BaseUseCase<Unit, List<OnboardingStep>>() {
    override suspend fun run(params: Unit): Result<List<OnboardingStep>> {
        return Result.Success(
            listOf(
                OnboardingStep(
                    image = R.drawable.ic_onboarding_1,
                    title = R.string.onboarding_title_1,
                    subtitle = R.string.onboarding_subtitle_1,
                    content = R.string.onboarding_content_1,
                    news = R.string.onboarding_news_1
                ),
                OnboardingStep(
                    image = R.drawable.ic_onboarding_2,
                    title = R.string.onboarding_title_2,
                    news = R.string.onboarding_news_2
                ),
                OnboardingStep(
                    image = R.drawable.ic_onboarding_3,
                    title = R.string.onboarding_title_3,
                    isInProgress = true
                )
            )
        )
    }
}
