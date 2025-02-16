package com.muliamaulana.github.utils

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

/**
 * Created by muliamaulana on 15/02/25.
 */

class CorePagingAdapter<T : Any> : PagingDataAdapter<T, CorePagingViewHolder<T>>(
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }) {

    @SuppressLint("DiffUtilEquals")
    val differCallBack = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    }

    lateinit var createViewHolder: ((ViewGroup) -> ViewBinding)
    lateinit var viewHolderBinding: ((T?, ViewBinding) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorePagingViewHolder<T> {
        return CorePagingViewHolder(createViewHolder(parent), viewHolderBinding)
    }

    override fun onBindViewHolder(holder: CorePagingViewHolder<T>, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

}