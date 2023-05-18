package com.wolfpackdigital.cashli.presentation.home

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.BankShortDetailsSectionBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.response.BankAccount
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class BankShortDetailsSectionAdapter :
    BaseAdapter<BankAccount, BankShortDetailsSectionBinding>(
        itemLayout = R.layout.bank_short_details_section_layout,
        diffUtil = object : DiffUtil.ItemCallback<BankAccount>() {
            override fun areItemsTheSame(
                oldItem: BankAccount,
                newItem: BankAccount
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: BankAccount,
                newItem: BankAccount
            ) = oldItem.name == newItem.name
        }
    ) {

    override fun bind(binding: BankShortDetailsSectionBinding, item: BankAccount) {}
}
