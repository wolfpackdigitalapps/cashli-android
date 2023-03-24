package com.wolfpackdigital.cashli.shared.utils.persistence

import com.orhanobut.hawk.Hawk
import com.wolfpackdigital.cashli.domain.entities.enums.Languages

interface PersistentService {

    var language: Languages?
        get() = Hawk.get(HawkKeys.LANGUAGE)
        set(value) {
            Hawk.put(HawkKeys.LANGUAGE, value)
        }
}
