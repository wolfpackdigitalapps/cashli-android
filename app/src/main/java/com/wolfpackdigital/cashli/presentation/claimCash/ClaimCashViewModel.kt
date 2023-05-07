package com.wolfpackdigital.cashli.presentation.claimCash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethod
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethodItem
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

class ClaimCashViewModel : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    val amountPerc = MutableLiveData(0)

    private val _amount = MutableLiveData(amountPerc.value?.toFloat() ?: 0f)
    val amount: LiveData<Float> = _amount

    private val _dueDate = MutableLiveData("February 15th, 2023")
    val dueDate: LiveData<String> = _dueDate

    private val _deliveryMethods = MutableLiveData<List<DeliveryMethodItem>>(listOf())
    val deliveryMethods: LiveData<List<DeliveryMethodItem>> = _deliveryMethods
    val selectedDeliveryMethod: LiveData<DeliveryMethodItem> = _deliveryMethods.map { list ->
        list.first { it.isSelected }
    }

    init {
        // TODO Replace this with appropriate data after clearing with BE
        _deliveryMethods.value = DeliveryMethod.values().map {
            DeliveryMethodItem(
                deliveryMethod = it,
                cost = "Free",
                isSelected = it == DeliveryMethod.EXPRESS
            )
        }
    }

    fun saveAmount() {
        amountPerc.value?.let {
            _amount.value = it.toFloat()
            _cmd.value = Command.TransitionToStart
        }
    }

    fun onDeliveryMethodSelected(item: DeliveryMethodItem) {
        val currentList = _deliveryMethods.value?.toMutableList()
        currentList?.let { deliveryMethods ->
            val newList = buildList {
                deliveryMethods.forEach { add(it.copy(isSelected = it == item)) }
            }
            _deliveryMethods.value = newList
        }
    }

    fun continueToQuiz() {
        _amount.value?.let {
            _baseCmd.value = BaseCommand.PerformNavAction(
                ClaimCashFragmentDirections.actionClaimCashFragmentToQuizFragment(it)
            )
        }
    }

    sealed class Command {
        object TransitionToStart : Command()
    }
}
