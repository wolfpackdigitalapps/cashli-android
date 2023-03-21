package com.wolfpackdigital.cashli.presentation.main.list

import androidx.recyclerview.widget.DiffUtil
import com.wolfpackdigital.cashli.MockBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.MockItem
import com.wolfpackdigital.cashli.shared.base.BaseAdapter

class MockListAdapter : BaseAdapter<MockItem, MockBinding>(
    R.layout.item_mock,
    object : DiffUtil.ItemCallback<MockItem>() {
        override fun areItemsTheSame(oldItem: MockItem, newItem: MockItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MockItem, newItem: MockItem) =
            oldItem == newItem
    }
) {

    var onClick: (MockItem) -> Unit = {}

    override fun bind(binding: MockBinding, item: MockItem) {
        binding.root.setOnClickListener { onClick(item) }
    }
}
