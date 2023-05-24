package com.wolfpackdigital.cashli.presentation.home

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.BankAccountSectionBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.LinkBankAccountInfo
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class BankAccountSectionAdapter :
    BaseAdapter<LinkBankAccountInfo, BankAccountSectionBinding>(
        itemLayout = R.layout.bank_account_section_layout,
        diffUtil = object : DiffUtil.ItemCallback<LinkBankAccountInfo>() {
            override fun areItemsTheSame(
                oldItem: LinkBankAccountInfo,
                newItem: LinkBankAccountInfo
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: LinkBankAccountInfo,
                newItem: LinkBankAccountInfo
            ) = oldItem.eligibilityStatus == newItem.eligibilityStatus &&
                oldItem.bankAccount?.name == newItem.bankAccount?.name
        }
    ) {

    override fun bind(binding: BankAccountSectionBinding, item: LinkBankAccountInfo) {}
}
