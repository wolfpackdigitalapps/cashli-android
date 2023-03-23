package com.wolfpackdigital.cashli.shared.utils.persistence

import com.orhanobut.hawk.Hawk

interface PersistentService {

    var language: String?
        get() = Hawk.get(HawkKeys.LANGUAGE)
        set(value) {
            Hawk.put(HawkKeys.LANGUAGE, value)
        }
}
