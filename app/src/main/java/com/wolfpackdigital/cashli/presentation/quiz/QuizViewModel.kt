package com.wolfpackdigital.cashli.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.extensions.percentOf

@Suppress("MagicNumber", "ForbiddenComment")
class QuizViewModel(cashAmount: Float) : BaseViewModel() {

    private val _tipSeekbarVisible = MutableLiveData(false)
    val tipSeekbarVisible: LiveData<Boolean> = _tipSeekbarVisible

    private val _sliderValue = MutableLiveData(0f)
    val sliderValue: LiveData<Float> = _sliderValue
    // TODO: Change this to actual value
    val tipAmountPerc: LiveData<Float> = _sliderValue.map { it / 10f }
    val tipAmount: LiveData<Float> = tipAmountPerc.map { it.percentOf(cashAmount) }

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

    fun setSecondAltQuestionVisible(visible: Boolean) {
        _displayAltSecondQuestion.value = visible
    }

    fun setSliderValue(sliderValue: Float) {
        _sliderValue.value = sliderValue
    }
}
