package com.wolfpackdigital.cashli.presentation.claimCash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethod
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethodItem
import com.wolfpackdigital.cashli.domain.entities.claimCash.TransferFees
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityChecks
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.usecases.GetCashAdvancesLimitsUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetTransferFeesUseCase
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import kotlinx.coroutines.flow.combine
import java.time.LocalTime

private const val THREE_O_CLOCK = 15

class ClaimCashViewModel(
    private val getTransferFees: GetTransferFeesUseCase,
    private val getCashAdvancesLimitsUseCase: GetCashAdvancesLimitsUseCase
) : BaseViewModel(), PersistenceService {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _userProfile = MutableLiveData(userProfile)
    val userProfileData: LiveData<UserProfile?> = _userProfile

    private val _labelAmount = MutableLiveData(0f)
    val labelAmount: LiveData<Float> = _labelAmount

    private val _amount = MutableLiveData(0f)
    val amount: LiveData<Float> = _amount

    private val _selectedDeliveryMethod = MutableLiveData(
        if (isAfter3PM())
            DeliveryMethod.EXPRESS_WITHIN_20_HOURS
        else
            DeliveryMethod.EXPRESS_SEVERAL_HOURS
    )
    val selectedDeliveryMethod: LiveData<DeliveryMethod> = _selectedDeliveryMethod

    private val transferFees = MutableLiveData<List<TransferFees>>()

    private val _cashAdvancesLimits = MutableLiveData<EligibilityChecks>()
    val cashAdvancesLimits: LiveData<EligibilityChecks> = _cashAdvancesLimits

    val deliveryMethods =
        combine(
            transferFees.asFlow(),
            _labelAmount.asFlow(),
            _selectedDeliveryMethod.asFlow()
        ) { transferFees, amount, selectedDeliveryMethod ->
            DeliveryMethod.values().map { type ->
                val fees = transferFees.find { amount in it.lowerLimit..it.upperLimit }
                val isDisabled = when (type) {
                    DeliveryMethod.EXPRESS_SEVERAL_HOURS -> isAfter3PM()
                    else -> false
                }
                val cost = when (type) {
                    DeliveryMethod.REGULAR -> fees?.regularFee ?: String()
                    DeliveryMethod.EXPRESS_SEVERAL_HOURS -> fees?.sameDayFee ?: String()
                    DeliveryMethod.EXPRESS_WITHIN_20_HOURS -> fees?.nextDayFee ?: String()
                    else -> String()
                }
                DeliveryMethodItem(
                    deliveryMethod = type,
                    cost = cost,
                    isSelected = type == selectedDeliveryMethod,
                    isDisabled = isDisabled
                )
            }
        }.asLiveData()

    init {
        getTransferFees()
        getEligibilityStatus()
    }

    private fun getEligibilityStatus() {
        performApiCall {
            val result = getCashAdvancesLimitsUseCase(Unit)
            result.onSuccess { eligibility ->
                _cashAdvancesLimits.value = eligibility
                _amount.value = eligibility.minCashAdvance?.toFloat() ?: 0f
                _labelAmount.value = eligibility.minCashAdvance?.toFloat() ?: 0f
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    private fun getTransferFees() {
        performApiCall {
            val result = getTransferFees(Unit)
            result.onSuccess {
                transferFees.value = it
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    fun saveAmount() {
        labelAmount.value?.let {
            _amount.value = it
            _cmd.value = Command.TransitionToStart
        }
    }

    fun onDeliveryMethodSelected(item: DeliveryMethodItem) {
        _selectedDeliveryMethod.value = item.deliveryMethod
    }

    fun continueToQuiz() {
        safeLet(_amount.value, _selectedDeliveryMethod.value) { amount, deliveryMethod ->
            _baseCmd.value = BaseCommand.PerformNavAction(
                ClaimCashFragmentDirections.actionClaimCashFragmentToQuizFragment(
                    cashAmount = amount,
                    deliveryMethod = deliveryMethod
                )
            )
        }
    }

    fun setLabelAmount(amountPerc: Float) {
        _labelAmount.value = amountPerc
    }

    private fun isAfter3PM() = LocalTime.now().isAfter(LocalTime.of(THREE_O_CLOCK, 0, 0))

    sealed class Command {
        object TransitionToStart : Command()
    }
}
