package com.wolfpackdigital.cashli.presentation.home

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.GenericWarningBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.HomeGenericWarningInfo
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class GenericWarningAdapter :
    BaseAdapter<HomeGenericWarningInfo, GenericWarningBinding>(
        itemLayout = R.layout.generic_warning_layout,
        diffUtil = object : DiffUtil.ItemCallback<HomeGenericWarningInfo>() {
            override fun areItemsTheSame(
                oldItem: HomeGenericWarningInfo,
                newItem: HomeGenericWarningInfo
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: HomeGenericWarningInfo,
                newItem: HomeGenericWarningInfo
            ) = oldItem.warningTextId == newItem.warningTextId
        }
    ) {
    override fun bind(binding: GenericWarningBinding, item: HomeGenericWarningInfo) {}
}