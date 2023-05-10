package com.wolfpackdigital.cashli.presentation.entities.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wolfpackdigital.cashli.R

enum class MenuItem(@StringRes val title: Int, @DrawableRes val icon: Int) {
    MEMBERSHIP_ADVANCE_HISTORY(R.string.membership_advance_history, R.drawable.ic_arrowhead_left),
    TERMS_AND_CONDITIONS(R.string.terms_and_conditions_title, R.drawable.ic_file),
    PRIVACY_POLICY(R.string.privacy_policy, R.drawable.ic_file),
    SETTINGS(R.string.settings, R.drawable.ic_settings),
    HELP(R.string.help, R.drawable.ic_hand),
    REFER_FRIEND(R.string.refer_friend, R.drawable.ic_plus_square),
    FAQ(R.string.faq, R.drawable.ic_question_mark),
    PAUSE_CLOSE_ACCOUNT(R.string.pause_close_account, R.drawable.ic_square_circle),
    UNPAUSE_CLOSE_ACCOUNT(R.string.unpause_close_account, R.drawable.ic_square_circle),
    LOGOUT(R.string.logout, R.drawable.ic_logout)
}
