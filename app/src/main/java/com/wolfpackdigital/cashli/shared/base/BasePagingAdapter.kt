package com.wolfpackdigital.cashli.shared.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wolfpackdigital.cashli.BR

abstract class BasePagingAdapter<ITEM : Any, BINDING : ViewDataBinding>(
    @LayoutRes private val itemLayout: Int,
    diffUtil: DiffUtil.ItemCallback<ITEM>
) : PagingDataAdapter<ITEM, BasePagingAdapter.BaseViewHolder<ViewDataBinding>>(diffUtil) {

    @CallSuper
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<BINDING>(
            LayoutInflater.from(parent.context),
            itemLayout,
            parent,
            false
        )
        return BaseViewHolder(binding)
    }

    @CallSuper
    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
        val item = getItem(position)
        holder.binding.setVariable(BR.viewModel, item)
        bind(holder.binding, item, position)
        holder.binding.executePendingBindings()
    }

    abstract fun bind(binding: ViewDataBinding, item: ITEM?, position: Int)

    class BaseViewHolder<out BINDING : ViewDataBinding>(val binding: BINDING) :
        RecyclerView.ViewHolder(binding.root)
}
