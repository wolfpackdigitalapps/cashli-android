package com.wolfpackdigital.cashli.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.shared.base.BaseViewModel

class QuizViewModel : BaseViewModel() {

    val tipAmountPerc = MutableLiveData(0)

    private val _displayAltSecondQuestion = MutableLiveData(false)
    val displayAltSecondQuestion: LiveData<Boolean> = _displayAltSecondQuestion

    fun onContinueClicked() {
        return
    }
}
