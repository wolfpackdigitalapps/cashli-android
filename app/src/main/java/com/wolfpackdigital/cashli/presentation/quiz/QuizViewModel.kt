package com.wolfpackdigital.cashli.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethod
import com.wolfpackdigital.cashli.domain.entities.requests.CashAdvanceRequest
import com.wolfpackdigital.cashli.domain.entities.requests.QuizAnswer
import com.wolfpackdigital.cashli.domain.entities.response.CashAdvanceDetails
import com.wolfpackdigital.cashli.domain.usecases.RequestCashAdvanceUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.quiz.QuizFragment.Companion.QUIZ_FIRST_QUESTION_ID
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.percentOf
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDate
import com.wolfpackdigital.cashli.shared.utils.views.MAX_TIP_SLIDER

private const val INITIAL_SLIDER_TIP = 7f
private const val QUIZ_ANSWERS_COUNT = 2

class QuizViewModel(
    private val cashAmount: Float,
    private val deliveryMethod: DeliveryMethod,
    private val requestCashAdvanceUseCase: RequestCashAdvanceUseCase
) : BaseViewModel() {

    private val _tipSeekbarVisible = MutableLiveData(false)
    val tipSeekbarVisible: LiveData<Boolean> = _tipSeekbarVisible

    private val _sliderValue = MutableLiveData(INITIAL_SLIDER_TIP)
    val sliderValue: LiveData<Float> = _sliderValue

    private val _displayAltSecondQuestion = MutableLiveData(false)
    val displayAltSecondQuestion: LiveData<Boolean> = _displayAltSecondQuestion

    private val _tipPercAmounts = MutableLiveData(getTipsList())
    val tipPercAmounts: LiveData<List<TipAmount>> = _tipPercAmounts

    private val _quizAnswers = MutableLiveData<List<QuizAnswer>>()
    val quizAnswers: LiveData<List<QuizAnswer>> = _quizAnswers

    val isContinueButtonEnabled = quizAnswers.map { it.size == QUIZ_ANSWERS_COUNT }

    private val selectedTipPercAmount = MutableLiveData<TipAmount>()

    val tipAmount = MediatorLiveData<Float>().apply {
        addSource(_sliderValue) {
            value = (MAX_TIP_SLIDER - it).percentOf(cashAmount)
        }
        addSource(selectedTipPercAmount) { tipAmount ->
            value = tipAmount.value?.let { value ->
                (value).percentOf(cashAmount)
            } ?: (MAX_TIP_SLIDER - (sliderValue.value ?: INITIAL_SLIDER_TIP)).percentOf(cashAmount)
        }
    }

    fun onTipAmountSelected(tipAmountIndex: Int) {
        val aux = _tipPercAmounts.value ?: listOf()
        aux.onEachIndexed { index, tipAmount ->
            if (tipAmountIndex == index)
                selectedTipPercAmount.value = tipAmount

            tipAmount.isChecked = tipAmountIndex == index
        }
        _tipSeekbarVisible.value = aux.any { it.isChecked && it.value == null }
        _tipPercAmounts.value = aux
    }

    private fun displayPopup(cashAdvanceDetails: CashAdvanceDetails) {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.congrats,
                imageId = R.drawable.ic_congrats,
                contentIdOrString = R.string.quiz_popup_description,
                contentFormatArgs = arrayOf(
                    cashAdvanceDetails.dueDate.toFormattedLocalDate() ?: Constants.DASH,
                    cashAdvanceDetails.totalRepayable
                ),
                secondaryContent = R.string.quiz_popup_annex,
                buttonCloseClick = {
                    _baseCmd.value = BaseCommand.GoBackTo(R.id.homeFragment)
                }
            )
        )
    }

    fun onContinueClicked() {
        safeLet(tipAmount.value, quizAnswers.value) { tip, answers ->
            performApiCall {
                val result = requestCashAdvanceUseCase(
                    CashAdvanceRequest(
                        amount = cashAmount,
                        tip = tip,
                        transferChannel = deliveryMethod,
                        quizAnswers = answers
                    )
                )
                result.onSuccess { cashAdvanceDetails ->
                    displayPopup(cashAdvanceDetails)
                }
                result.onError {
                    val error =
                        it.errors?.firstOrNull() ?: it.message ?: R.string.generic_error
                    _baseCmd.value = BaseCommand.ShowToast(error)
                }
            }
        }
    }

    fun setQuizAnswer(quizAnswer: QuizAnswer) {
        _quizAnswers.value =
            buildList {
                if (quizAnswer.questionId != QUIZ_FIRST_QUESTION_ID) {
                    val oldAnswer = quizAnswers.value?.find { oldAnswer ->
                        oldAnswer.questionId == quizAnswer.questionId
                    }
                    addAll(quizAnswers.value ?: emptyList())
                    oldAnswer?.let {
                        remove(oldAnswer)
                    }
                }
                add(quizAnswer)
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
        const val TIP_8 = 8
        fun getTipsList() =
            buildList {
                add(TipAmount(TIP_4, false))
                add(TipAmount(TIP_5, false))
                add(TipAmount(TIP_8, true))
                add(TipAmount(null, false))
            }
    }
}
