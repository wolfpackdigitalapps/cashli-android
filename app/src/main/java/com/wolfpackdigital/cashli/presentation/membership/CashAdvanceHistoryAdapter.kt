package com.wolfpackdigital.cashli.presentation.membership

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.MembershipAdvanceItemBinding
import com.wolfpackdigital.cashli.shared.base.BasePagingAdapter

class CashAdvanceHistoryAdapter :
    BasePagingAdapter<MembershipAdvanceItem, MembershipAdvanceItemBinding>(
        R.layout.item_membership_advance,
        object : DiffUtil.ItemCallback<MembershipAdvanceItem>() {
            override fun areItemsTheSame(oldItem: MembershipAdvanceItem, newItem: MembershipAdvanceItem) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: MembershipAdvanceItem, newItem: MembershipAdvanceItem) =
                oldItem == newItem
        }
    ) {
    override fun bind(binding: ViewDataBinding, item: MembershipAdvanceItem?, position: Int) {}
}
