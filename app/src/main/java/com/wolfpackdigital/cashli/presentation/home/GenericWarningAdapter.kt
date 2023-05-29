package com.wolfpackdigital.cashli.presentation.home

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.GenericWarningBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.GenericWarningInfo
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class GenericWarningAdapter :
    BaseAdapter<GenericWarningInfo, GenericWarningBinding>(
        itemLayout = R.layout.generic_warning_layout,
        diffUtil = object : DiffUtil.ItemCallback<GenericWarningInfo>() {
            override fun areItemsTheSame(
                oldItem: GenericWarningInfo,
                newItem: GenericWarningInfo
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: GenericWarningInfo,
                newItem: GenericWarningInfo
            ) = oldItem.warningTextId == newItem.warningTextId
        }
    ) {
    override fun bind(binding: GenericWarningBinding, item: GenericWarningInfo) {}
}
