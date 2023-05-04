package com.wolfpackdigital.cashli.presentation.linkBank.ineligible

import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel

class IneligibleInformativeViewModel : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(onBack = ::back)
    )
    val toolbar: LiveData<Toolbar> = _toolbar

}