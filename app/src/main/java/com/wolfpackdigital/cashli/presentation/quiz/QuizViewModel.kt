package com.wolfpackdigital.cashli.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethod
import com.wolfpackdigital.cashli.domain.entities.requests.CashAdvanceRequest
import com.wolfpackdigital.cashli.domain.usecases.RequestCashAdvanceUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.extensions.percentOf

private const val INITIAL_SLIDER_TIP = 8f

class QuizViewModel(
    private val cashAmount: Float,
    private val deliveryMethod: DeliveryMethod,
    private val requestCashAdvance: RequestCashAdvanceUseCase
) : BaseViewModel() {

    private val _tipSeekbarVisible = MutableLiveData(false)
    val tipSeekbarVisible: LiveData<Boolean> = _tipSeekbarVisible

    private val _sliderValue = MutableLiveData(INITIAL_SLIDER_TIP)
    val sliderValue: LiveData<Float> = _sliderValue
    val tipAmount: LiveData<Float> = _sliderValue.map { it.percentOf(cashAmount) }

    private val _displayAltSecondQuestion = MutableLiveData(false)
    val displayAltSecondQuestion: LiveData<Boolean> = _displayAltSecondQuestion

    private val _tipPercAmounts = MutableLiveData(getTipsList())
    val tipPercAmounts: LiveData<List<TipAmount>> = _tipPercAmounts

    fun onTipAmountSelected(tipAmountIndex: Int) {
        val aux = _tipPercAmounts.value ?: listOf()
        aux.onEachIndexed { index, tipAmount ->
            tipAmount.isChecked = tipAmountIndex == index
        }
        _tipSeekbarVisible.value = aux.any { it.isChecked && it.value == null }
        _tipPercAmounts.value = aux
    }

    private fun displayPopup() {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleId = R.string.congrats,
                imageId = R.drawable.ic_congrats,
                contentIdOrString = R.string.quiz_popup_description,
                contentFormatArgs = arrayOf(
                    "14 February 2023",
                    "${cashAmount + (tipAmount.value ?: 0f)}"
                ),
                secondaryContent = R.string.quiz_popup_annex,
                buttonCloseClick = {
                    _baseCmd.value = BaseCommand.GoBackTo(R.id.homeFragment)
                }
            )
        )
    }

    fun onContinueClicked() {
        tipAmount.value?.let { tip ->
            performApiCall {
                val result = requestCashAdvance(
                    CashAdvanceRequest(
                        amount = cashAmount,
                        tip = tip,
                        transferChannel = deliveryMethod

                    )
                )
                result.onSuccess { displayPopup() }
                result.onError {
                    val error =
                        it.errors?.firstOrNull() ?: it.message ?: R.string.generic_error
                    _baseCmd.value = BaseCommand.ShowToast(error)
                }
            }
        }
    }

    fun setSecondAltQuestionVisible(visible: Boolean) {
        _displayAltSecondQuestion.value = visible
    }

    fun setSliderValue(sliderValue: Float) {
        _sliderValue.value = sliderValue
    }

    private companion object {
        const val TIP_4 = 4
        const val TIP_5 = 5
        const val TIP_6 = 6
        fun getTipsList() =
            buildList {
                add(TipAmount(TIP_4, true))
                add(TipAmount(TIP_5, false))
                add(TipAmount(TIP_6, false))
                add(TipAmount(null, false))
            }
    }
}
