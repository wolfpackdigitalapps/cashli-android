package com.wolfpackdigital.cashli.presentation.membership

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.MembershipAdvanceItemBinding
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class MembershipAdapter :
    BaseAdapter<MembershipAdvanceItem, MembershipAdvanceItemBinding>(
        itemLayout = R.layout.item_membership_advance,
        diffUtil = object : DiffUtil.ItemCallback<MembershipAdvanceItem>() {
            override fun areItemsTheSame(
                oldItem: MembershipAdvanceItem,
                newItem: MembershipAdvanceItem
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MembershipAdvanceItem,
                newItem: MembershipAdvanceItem
            ) = oldItem == newItem
        }
    ) {

    override fun bind(binding: MembershipAdvanceItemBinding, item: MembershipAdvanceItem) {}
}
