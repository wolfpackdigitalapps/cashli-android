package com.wolfpackdigital.cashli.shared.utils.persistence

import com.orhanobut.hawk.Hawk
import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.domain.entities.response.Token
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.utils.extensions.fromJson
import com.wolfpackdigital.cashli.shared.utils.extensions.json

interface PersistenceService {

    var token: Token?
        get() = Hawk.get(HawkKeys.TOKEN_KEY)
        set(token) {
            Hawk.put(HawkKeys.TOKEN_KEY, token)
        }

    var deviceToken: String?
        get() = Hawk.get(HawkKeys.KEY_DEVICE_TOKEN)
        set(token) {
            Hawk.put(HawkKeys.KEY_DEVICE_TOKEN, token)
        }
    var isNotificationPermissionAsked: Boolean
        get() = Hawk.get(HawkKeys.NOTIFICATION_PERMISSION_ASKED, false)
        set(token) {
            Hawk.put(HawkKeys.NOTIFICATION_PERMISSION_ASKED, token)
        }

    var hasNotificationPermissionGranted: Boolean
        get() = Hawk.get(HawkKeys.NOTIFICATION_PERMISSION_GRANTED, false)
        set(token) {
            Hawk.put(HawkKeys.NOTIFICATION_PERMISSION_GRANTED, token)
        }

    var language: Language?
        get() = Hawk.get(HawkKeys.LANGUAGE)
        set(value) {
            Hawk.put(HawkKeys.LANGUAGE, value)
        }

    var userProfile: UserProfile?
        get() {
            val user = Hawk.get<UserProfile?>(HawkKeys.USER_PROFILE)
            return user?.copy(
                userSettings = user.userSettings.json().fromJson() ?: listOf()
            )
        }
        set(value) {
            Hawk.put(HawkKeys.USER_PROFILE, value)
        }
    fun clearUserData() {
        Hawk.delete(HawkKeys.USER_PROFILE)
        Hawk.delete(HawkKeys.TOKEN_KEY)
    }
}
