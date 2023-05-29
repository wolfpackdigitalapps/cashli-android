package com.wolfpackdigital.cashli.presentation.more.deleteAccount

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.ItemRadioButtonBinding
import com.wolfpackdigital.cashli.presentation.entities.SimpleSelectableItem
import com.wolfpackdigital.cashli.shared.base.BaseAdapter
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setOnClickDebounced

class DeleteAccountReasonsAdapter(private val onItemClicked: (SimpleSelectableItem, String) -> Unit) :
    BaseAdapter<SimpleSelectableItem, ItemRadioButtonBinding>(
        itemLayout = R.layout.item_radio_button,
        diffUtil = object : DiffUtil.ItemCallback<SimpleSelectableItem>() {
            override fun areItemsTheSame(
                oldItem: SimpleSelectableItem,
                newItem: SimpleSelectableItem
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SimpleSelectableItem,
                newItem: SimpleSelectableItem
            ) = oldItem.isChecked == newItem.isChecked
        }
    ) {

    override fun bind(binding: ItemRadioButtonBinding, item: SimpleSelectableItem) {
        binding.clRoot.setOnClickDebounced {
            val itemValueHumanized = binding.root.context.getString(item.value)
            onItemClicked(item, itemValueHumanized)
        }
    }
}
