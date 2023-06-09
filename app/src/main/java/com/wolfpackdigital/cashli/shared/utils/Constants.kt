package com.wolfpackdigital.cashli.shared.utils

object Constants {
    // Millis
    const val DEBOUNCE_INTERVAL_MILLIS_1000 = 1_000L
    const val DEBOUNCE_INTERVAL_MILLIS_300 = 350L
    const val COUNT_DOWN_TIME_6_SEC = 6_000L
    const val COUNT_DOWN_TIME_30_SEC = 30

    // MISC
    const val FIRST_INDEX = 0
    const val STEP_1 = 1
    const val STEP_2 = 2
    const val STEP_3 = 3
    const val REPEAT_ANIM_ONE_TIME = 1
    const val ALPHA_1 = 1f
    const val ALPHA_0 = 0f
    const val EMPTY_STRING = ""
    const val COMMA = ","
    const val VARIANT_DEVELOP = "develop"
    const val DOUBLE_SPACE = "  "
    const val DEFAULT_PAGE_INDEX = 1
    const val TRANSACTIONS_PAGE_SIZE = 10
    const val SCROLL_TO_TOP = 0
    const val DASH = "-"

    // Input text constraints
    const val PHONE_PREFIX_US = "+1"
    const val PHONE_PREFIX_RO = "+40"
    const val PHONE_NUMBER_LENGTH = 10
    const val MAX_CHARS_50 = 50
    const val MIN_CHARS_2 = 2

    // Back stack keys
    const val RESTART_ONBOARDING_STEPS = "restart_onboarding_steps"
    const val REFRESH_USER_DATA = "refresh_user_data"

    // API ERROR CODES
    const val ERROR_CODE_401 = 401
    const val ERROR_CODE_409 = 409
    const val ERROR_CODE_422 = 422
    const val ERROR_CODE_429 = 429

    // DeepLinks
    const val SIGN_IN_SCREEN_DL = "android-app://com.wolfpackdigital.cashli/signIn"
    const val INELIGIBLE_INFORMATIVE_SCREEN_DL = "android-app://com.wolfpackdigital.cashli/ineligibleInformative"
    const val CONFIRM_ONE_TIME_PASSWORD_DL = "android-app://com.wolfpackdigital.cashli/confirmOneTimePassword/"

    // Support
    const val SUPPORT_PHONE_NUMBER = "+18722252743"

    // DateTime Formatter
    const val FULL_MONTH_DAY_YEAR = "d MMMM yyyy"
    const val FULL_MONTH_DAY_YEAR_EN = "MMMM d, yyyy"

    // PUSH NOTIFICATIONS
    const val PUSH_NOTIFICATION_EXTRA = "push_notification_extra"
    const val PUSH_NOTIFICATION_EXTRA_DATA = "push_notification_extra_data"
    const val PUSH_NOTIFICATION_EXTRA_FOREGROUND = "push_notification_extra_foreground"
    const val PUSH_NOTIFICATION_EXTRA_DATA_FOREGROUND = "push_notification_extra_data_foreground"
}
