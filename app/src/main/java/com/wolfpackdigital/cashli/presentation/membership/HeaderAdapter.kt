package com.wolfpackdigital.cashli.presentation.membership

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.HeaderItemBinding
import com.wolfpackdigital.cashli.presentation.entities.HeaderItem
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class HeaderAdapter :
    BaseAdapter<HeaderItem, HeaderItemBinding>(
        itemLayout = R.layout.item_header,
        diffUtil = object : DiffUtil.ItemCallback<HeaderItem>() {
            override fun areItemsTheSame(
                oldItem: HeaderItem,
                newItem: HeaderItem
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: HeaderItem,
                newItem: HeaderItem
            ) = oldItem.title == newItem.title
        }
    ) {

    override fun bind(binding: HeaderItemBinding, item: HeaderItem) {}
}
