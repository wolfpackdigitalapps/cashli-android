package com.wolfpackdigital.cashli.shared.utils.persistence

import com.orhanobut.hawk.Hawk
import com.wolfpackdigital.cashli.domain.entities.enums.Languages
import com.wolfpackdigital.cashli.domain.entities.response.Token
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile

interface PersistenceService {

    var token: Token?
        get() = Hawk.get(HawkKeys.TOKEN_KEY)
        set(token) {
            Hawk.put(HawkKeys.TOKEN_KEY, token)
        }

    var language: Languages?
        get() = Hawk.get(HawkKeys.LANGUAGE)
        set(value) {
            Hawk.put(HawkKeys.LANGUAGE, value)
        }

    var userProfile: UserProfile?
        get() = Hawk.get(HawkKeys.USER_PROFILE)
        set(value) {
            Hawk.put(HawkKeys.USER_PROFILE, value)
        }
}
