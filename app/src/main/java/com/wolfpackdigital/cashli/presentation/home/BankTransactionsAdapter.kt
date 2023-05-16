package com.wolfpackdigital.cashli.presentation.home

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.ItemBankTransactionBinding
import com.wolfpackdigital.cashli.domain.entities.response.BankTransaction
import com.wolfpackdigital.cashli.shared.base.BasePagingAdapter

class BankTransactionsAdapter :
    BasePagingAdapter<BankTransaction, ItemBankTransactionBinding>(
        R.layout.item_bank_transaction,
        object : DiffUtil.ItemCallback<BankTransaction>() {
            override fun areItemsTheSame(oldItem: BankTransaction, newItem: BankTransaction) =
                oldItem.transactionId == newItem.transactionId

            override fun areContentsTheSame(oldItem: BankTransaction, newItem: BankTransaction) =
                oldItem == newItem
        }
    ) {
    override fun bind(binding: ViewDataBinding, item: BankTransaction?, position: Int) {}
}
