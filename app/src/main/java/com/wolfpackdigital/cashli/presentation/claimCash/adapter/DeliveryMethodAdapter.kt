package com.wolfpackdigital.cashli.presentation.claimCash.adapter

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.DeliveryMethodItemBinding
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethodItem
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class DeliveryMethodAdapter(private val onItemClicked: (DeliveryMethodItem) -> Unit) :
    BaseAdapter<DeliveryMethodItem, DeliveryMethodItemBinding>(
        itemLayout = R.layout.item_delivery_method,
        diffUtil = object : DiffUtil.ItemCallback<DeliveryMethodItem>() {
            override fun areItemsTheSame(
                oldItem: DeliveryMethodItem,
                newItem: DeliveryMethodItem
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DeliveryMethodItem,
                newItem: DeliveryMethodItem
            ) = oldItem.deliveryMethod == newItem.deliveryMethod
        }
    ) {

    override fun bind(binding: DeliveryMethodItemBinding, item: DeliveryMethodItem) {
        binding.rootCl.setOnClickListener { onItemClicked(item) }
    }
}
