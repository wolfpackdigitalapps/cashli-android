package com.wolfpackdigital.cashli.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.shared.base.BaseViewModel

class QuizViewModel : BaseViewModel() {

    private val _tipSeekbarVisible = MutableLiveData(false)
    val tipSeekbarVisible: LiveData<Boolean> = _tipSeekbarVisible
    val tipAmountPerc = MutableLiveData(0)

    private val _displayAltSecondQuestion = MutableLiveData(false)
    val displayAltSecondQuestion: LiveData<Boolean> = _displayAltSecondQuestion

    @Suppress("MagicNumber")
    private val _tipPercAmounts = MutableLiveData(
        buildList {
            add(TipAmount(4, true))
            add(TipAmount(5, false))
            add(TipAmount(6, false))
            add(TipAmount(null, false))
        }
    )
    val tipPercAmounts: LiveData<List<TipAmount>> = _tipPercAmounts

    fun onTipAmountSelected(tipAmountIndex: Int) {
        val aux = _tipPercAmounts.value ?: listOf()
        aux.onEachIndexed { index, tipAmount ->
            tipAmount.isChecked = tipAmountIndex == index
        }
        _tipSeekbarVisible.value = aux.any { it.isChecked && it.value == null }
        _tipPercAmounts.value = aux
    }

    fun onContinueClicked() {
        return
    }
}
