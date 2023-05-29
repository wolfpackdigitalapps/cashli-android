package com.wolfpackdigital.cashli.presentation.home

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.CashAdvanceSectionBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.RequestCashAdvanceInfo
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class CashAdvanceSectionAdapter :
    BaseAdapter<RequestCashAdvanceInfo, CashAdvanceSectionBinding>(
        itemLayout = R.layout.cash_advance_section_layout,
        diffUtil = object : DiffUtil.ItemCallback<RequestCashAdvanceInfo>() {
            override fun areItemsTheSame(
                oldItem: RequestCashAdvanceInfo,
                newItem: RequestCashAdvanceInfo
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: RequestCashAdvanceInfo,
                newItem: RequestCashAdvanceInfo
            ) = oldItem.cashApproved == newItem.cashApproved &&
                oldItem.cashAdvanceBalance == newItem.cashAdvanceBalance &&
                oldItem.upToSum == newItem.upToSum
        }
    ) {

    override fun bind(binding: CashAdvanceSectionBinding, item: RequestCashAdvanceInfo) {}
}
