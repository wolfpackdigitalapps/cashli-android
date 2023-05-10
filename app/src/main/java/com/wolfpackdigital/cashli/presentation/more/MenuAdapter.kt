package com.wolfpackdigital.cashli.presentation.more

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.MenuItemBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.enums.MenuItem
import com.wolfpackdigital.cashli.shared.base.BaseAdapter
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setOnClickDebounced

class MenuAdapter(
    private val onMenuItemClicked: (MenuItem) -> Unit
) : BaseAdapter<MenuItem, MenuItemBinding>(
    R.layout.item_menu,
    object : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean =
            oldItem == newItem
    }
) {
    override fun bind(binding: MenuItemBinding, item: MenuItem) {
        binding.clRoot.setOnClickDebounced {
            onMenuItemClicked(item)
        }
    }
}
