package com.wolfpackdigital.cashli.presentation.more.deleteAccount

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.SimpleSelectableItem
import com.wolfpackdigital.cashli.presentation.entities.TextSpanAction
import com.wolfpackdigital.cashli.presentation.entities.args.DeleteAccountArgs
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

private const val VALUE_SPAN_TEXT_US = "textUs"

class DeleteAccountViewModel(
    private val deleteAccountArgs: DeleteAccountArgs
) : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _closeAccountReasons = MutableLiveData(
        buildList {
            add(SimpleSelectableItem(R.string.close_account_reason_advance_low))
            add(SimpleSelectableItem(R.string.close_account_reason_fee))
            add(SimpleSelectableItem(R.string.close_account_reason_ineligible))
            add(SimpleSelectableItem(R.string.close_account_reason_competitor))
        }
    )
    val closeAccountReasons: LiveData<List<SimpleSelectableItem>> = _closeAccountReasons

    var closeAccountReason: String? = null

    @StringRes
    val otherReasonTextId: Int = R.string.other_reason
    val otherReasonSpanActions: List<TextSpanAction> = listOf(
        TextSpanAction(
            actionKey = VALUE_SPAN_TEXT_US,
            action = {
                _baseCmd.value = BaseCommand.OpenSMSApp()
            },
            isSpanTextUnderlined = true,
            spanTextColor = R.color.colorPrimaryDark
        )
    )

    fun onCloseAccountClicked() {
        deleteAccountArgs.onDeleteAccount(closeAccountReason)
        back()
    }

    fun onCloseAccountReasonSelected(item: SimpleSelectableItem, itemValueHumanized: String) {
        closeAccountReason = if (item.isChecked) null else itemValueHumanized
        val new = closeAccountReasons.value?.map {
            if (it.value == item.value) item.copy(isChecked = !item.isChecked)
            else it.copy(isChecked = false)
        }
        _closeAccountReasons.value = new
    }

    sealed class Command
}
